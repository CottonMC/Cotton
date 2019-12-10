package io.github.cottonmc.cotton.cauldron.tweaker;

import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.cotton.cauldron.CauldronContext;
import io.github.cottonmc.libcd.api.CDLogger;
import io.github.cottonmc.libcd.api.CDSyntaxError;
import io.github.cottonmc.libcd.api.tweaker.ScriptBridge;
import io.github.cottonmc.libcd.api.tweaker.Tweaker;
import io.github.cottonmc.libcd.api.tweaker.recipe.RecipeParser;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Predicate;

//TODO: improve system
public class CauldronTweaker implements Tweaker {
	public final Map<Predicate<CauldronContext>, CauldronBehavior> behaviors = new HashMap<>();
	public static final CauldronTweaker INSTANCE = new CauldronTweaker();
	private CDLogger logger = new CDLogger();
	private JsonObject debug;

	/**
	 * Used during data pack loading to clear the behavior list.
	 * DO NOT CALL THIS YOURSELF, EVER. IT WILL WIPE ALL DATA PACK BEHAVIORS.
	 */
	public void prepareReload(ResourceManager manager) {
		INSTANCE.behaviors.clear();
		debug = new JsonObject();
	}

	@Override
	public void applyReload(ResourceManager resourceManager, Executor executor) { }

	@Override
	public String getApplyMessage() {
		return behaviors.size() + " cauldron " + (behaviors.size() == 1? "behavior" : "behaviors");
	}

	@Override
	public void prepareFor(ScriptBridge bridge) {
		this.logger = new CDLogger(bridge.getId().getNamespace());
	}

	@Override
	public JsonObject getDebugInfo() {
		return debug;
	}

	/**
	 * Register a new cauldron behavior.
	 * References functions defined in the same script as this is called.
	 * @param bridge The ScriptBridge passed in the `libcd` variable.
	 * @param testName The name of the function used to test whether this behavior should run. Passed one argument of type {@link WrappedCauldronContext}.
	 * @param runName The name of the function used to perform this behavior. Passed one argument of type {@link WrappedCauldronContext}.
	 */
	public void registerBehavior(ScriptBridge bridge, String testName, String runName) {
		ScriptEngine engine = bridge.getEngine();
		if (engine instanceof Invocable) {
			String key = bridge.getId().toString();
			if (!debug.containsKey(key)) {
				debug.put(key, new JsonArray());
			}
			JsonArray array = (JsonArray) debug.get(key);
			JsonObject toAdd = new JsonObject();
			toAdd.put("test_func", new JsonPrimitive(testName));
			toAdd.put("run_func", new JsonPrimitive(runName));
			array.add(toAdd);
			INSTANCE.behaviors.put(new ScriptedPredicate(bridge.getId(), (Invocable) engine, testName), new ScriptedBehavior(bridge.getId(), (Invocable) engine, runName));
		} else {
			logger.error("Cannot register cauldron behaviors using this language, the engine must be able to invoke functions in scripts");
		}
	}

	@Deprecated
	/**
	 * Register a new cauldron behavior. Deprecated; use {@link CauldronTweaker#registerBehavior(ScriptBridge, String, String)} instead.
	 * Construct new classes and pass them functions for these. Each are passed a {@link CauldronContext}.
	 * @param ctx A predicate for under what conditions the behavior should be performed.
	 * @param behavior The behavior to perform when the conditions are met.
	 */
	public void registerBehavior(Predicate<CauldronContext> context, CauldronBehavior behavior) {
		int deprecated = debug.getInt("deprecated_behaviors", 0);
		debug.put("deprecated_behaviors", new JsonPrimitive(deprecated + 1));
		INSTANCE.behaviors.put(context, behavior);
	}

	/**
	 * Drain a cauldron of its fluid (for ease of calling from scripts).
	 * @param context The {@link CauldronContext} being used, wrapped for use outside of obf.
	 * @param bottles How many bottles of fluid to drain.
	 * @return Whether the drain was successful.
	 */
	public boolean drainCauldron(WrappedCauldronContext context, int bottles) {
		CauldronContext ctx = context.getContext();
		return ctx.getCauldron().drain(ctx.getWorld(), ctx.getPos(), ctx.getState(), ctx.getFluid(), bottles);
	}

	/**
	 * Fill a cauldron with a given fluid (for ease of calling from scripts).
	 * @param context The {@link CauldronContext} being used, wrapped for use outside of obf.
	 * @param fluid What fluid to fill with.
	 * @param bottles How many bottles of fluid to fill.
	 * @return Whether the fill was successful.
	 */
	public boolean fillCaudron(WrappedCauldronContext context, Fluid fluid, int bottles) {
		CauldronContext ctx = context.getContext();
		return ctx.getCauldron().fill(ctx.getWorld(), ctx.getPos(), ctx.getState(), fluid, bottles);
	}

	/**
	 * Take items from the stack used in a cauldron behavior.
	 * @param context The {@link CauldronContext} being used, wrapped for use outside of obf.
	 * @param amount How many items to take.
	 * @return Whether the items could be taken.
	 */
	public boolean takeItem(WrappedCauldronContext context, int amount) {
		CauldronContext ctx = context.getContext();
		if (ctx.getStack().getCount() < amount) return false;
		if (ctx.getPlayer() == null || !ctx.getPlayer().abilities.creativeMode) ctx.getStack().decrement(amount);
		return true;
	}

	/**
	 * Give items to a player using a cauldron behavior, or drop them on the ground if there's no player.
	 * Items dropped will have the "NoCauldronCollect" scoreboard tag, which prevents in-spec cauldrons from picking them up hopper-style.
	 * @param context The {@link CauldronContext} being used.
	 * @param stack The item stack to give.
	 * @return Whether the items could be given.
	 */
	public boolean giveItem(WrappedCauldronContext context, Object stack) {
		CauldronContext ctx = context.getContext();
		PlayerEntity player = ctx.getPlayer();
		try {
			ItemStack give = RecipeParser.processItemStack(stack);
			if (player != null) {
				player.increaseStat(Stats.USE_CAULDRON, 1);
				if (ctx.getStack().isEmpty()) {
					player.setStackInHand(ctx.getHand(), give);
				} else if (!player.inventory.insertStack(give)) {
					ItemEntity entity = player.dropItem(give, false);
					if (entity != null) entity.addScoreboardTag("NoCauldronCollect");
					ctx.getWorld().spawnEntity(entity);
				}
			} else {
				ItemEntity entity = new ItemEntity(ctx.getWorld(), ctx.getPos().getX(), ctx.getPos().getY() + 1, ctx.getPos().getZ(), give);
				entity.addScoreboardTag("NoCauldronCollect");
				ctx.getWorld().spawnEntity(entity);
			}
			return true;
		} catch (CDSyntaxError e) {
			context.getLogger().error("Could not parse cauldron tweaker item stack: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Play a sound effect from the cauldron.
	 * @param context The {@link CauldronContext} being used, wrapped for use outside of obf.
	 * @param sound The ID of the sound to play.
	 * @param volume The volume to play at.
	 * @param pitch The pitch to play at.
	 */
	public void playSound(WrappedCauldronContext context, String sound, float volume, float pitch) {
		CauldronContext ctx = context.getContext();
		ctx.getWorld().playSound(null, ctx.getPos(), Registry.SOUND_EVENT.get(new Identifier(sound)), SoundCategory.BLOCKS, volume, pitch);
	}

	/**
	 * Spawn an entity above the cauldron.
	 * @param context The {@link CauldronContext} being used, wrapped for use outside of obf.
	 * @param typeName The ID of the type of entity to spawn.
	 */
	public void spawnEntity(WrappedCauldronContext context, String typeName) {
		CauldronContext ctx = context.getContext();
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
		EntityType<?> type = Registry.ENTITY_TYPE.get(new Identifier(typeName));
		if (type.equals(EntityType.LIGHTNING_BOLT) && world instanceof ServerWorld) {
			LightningEntity lightning = new LightningEntity(ctx.getWorld(), pos.getX(), pos.getY(), pos.getZ(), false);
			((ServerWorld)world).addLightning(lightning);
		} else {
			Entity entity = type.create(world);
			if (entity == null) return;
			if (type.getCategory() == EntityCategory.MONSTER) {
				((MobEntity)entity).initialize(world, world.getLocalDifficulty(pos), SpawnType.EVENT, null, null);
			}
			entity.setPosition(pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5);
			world.spawnEntity(entity);
		}
	}

}
