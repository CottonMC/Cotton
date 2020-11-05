package io.github.cottonmc.cotton.cauldron.tweaker;

import io.github.cottonmc.cotton.cauldron.CauldronContext;
import io.github.cottonmc.libdp.api.DPSyntaxError;
import io.github.cottonmc.libdp.api.driver.recipe.RecipeParser;
import io.github.cottonmc.libdp.api.driver.util.DriverUtils;
import io.github.cottonmc.libdp.api.util.StackInfo;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


public class WrappedCauldronContext {
	private CauldronContext context;
	private Logger logger;

	public WrappedCauldronContext(Logger logger, CauldronContext context) {
		this.context = context;
		this.logger = logger;
	}

	/**
	 * @return The current level of the cauldron's fluid, in bottles.
	 */
	public int getLevel() {
		return context.getLevel();
	}

	/**
	 * @return The info of the item stack being used to perform this behavior.
	 */
	public StackInfo getStack() {
		return new StackInfo(context.getStack());
	}

	/**
	 * @return The player hand being used to perform this behavior, either "main" or "off".
	 */
	public String getHand() {
		if (context.getHand().equals(Hand.MAIN_HAND)) return "main";
		else return "off";
	}

	/**
	 * @return The string ID of the fluid in this cauldron.
	 */
	public String getFluid() {
		return Registry.FLUID.getId(context.getFluid()).toString();
	}

	/**
	 * @return The items placed in this cauldron before the behavior was performed, if it's supported.
	 */
	public StackInfo[] getPreviousItems() {
		List<StackInfo> prev = new ArrayList<>();
		for (ItemStack stack : context.getPreviousItems()) {
			if (!stack.isEmpty()) prev.add(new StackInfo(stack));
		}
		return prev.toArray(new StackInfo[0]);
	}

	/**
	 * @return Whether there's an active fire under the cauldron.
	 */
	public boolean hasFireUnder() {
		return context.hasFireUnder();
	}

	/**
	 * @return The raw context of the cauldron behavior, not usable within a script.
	 */
	public CauldronContext getContext() {
		return context;
	}

	/**
	 * @return The logger to use when logging info or errors.
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Drain a cauldron of its fluid (for ease of calling from scripts).
	 * @param bottles How many bottles of fluid to drain.
	 * @return Whether the drain was successful.
	 */
	public boolean drain(int bottles) {
		return context.getCauldron().drain(context.getWorld(), context.getPos(), context.getState(), context.getFluid(), bottles);
	}

	/**
	 * Fill a cauldron with a given fluid (for ease of calling from scripts).
	 * @param fluid What fluid to fill with as its string ID.
	 * @param bottles How many bottles of fluid to fill.
	 * @return Whether the fill was successful.
	 */
	public boolean fill(String fluid, int bottles) {
		return fill(DriverUtils.INSTANCE.getRawFluid(fluid), bottles);
	}

	/**
	 * Fill a cauldron with a given fluid (for ease of calling from scripts).
	 * @param fluid What fluid to fill with.
	 * @param bottles How many bottles of fluid to fill.
	 * @return Whether the fill was successful.
	 */
	public boolean fill(Fluid fluid, int bottles) {
		return context.getCauldron().fill(context.getWorld(), context.getPos(), context.getState(), fluid, bottles);
	}

	/**
	 * Take items from the stack used in a cauldron behavior.
	 * @param amount How many items to take.
	 * @return Whether the items could be taken.
	 */
	public boolean takeItem(int amount) {
		if (context.getStack().getCount() < amount) return false;
		if (context.getPlayer() == null || !context.getPlayer().abilities.creativeMode) context.getStack().decrement(amount);
		return true;
	}

	/**
	 * Give items to a player using a cauldron behavior, or drop them on the ground if there's no player.
	 * Items dropped will have the "NoCauldronCollect" scoreboard tag, which prevents in-spec cauldrons from picking them up hopper-style.
	 * @param stack The item stack to give.
	 * @return Whether the items could be given.
	 */
	public boolean giveItem(Object stack) {
		PlayerEntity player = context.getPlayer();
		try {
			ItemStack give = RecipeParser.processItemStack(stack);
			if (player != null) {
				player.increaseStat(Stats.USE_CAULDRON, 1);
				if (context.getStack().isEmpty()) {
					player.setStackInHand(context.getHand(), give);
				} else if (!player.inventory.insertStack(give)) {
					ItemEntity entity = player.dropItem(give, false);
					if (entity != null) entity.addScoreboardTag("NoCauldronCollect");
					context.getWorld().spawnEntity(entity);
				}
			} else {
				ItemEntity entity = new ItemEntity(context.getWorld(), context.getPos().getX(), context.getPos().getY() + 1, context.getPos().getZ(), give);
				entity.addScoreboardTag("NoCauldronCollect");
				context.getWorld().spawnEntity(entity);
			}
			return true;
		} catch (DPSyntaxError e) {
			getLogger().error("Could not parse cauldron tweaker item stack: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Play a sound effect from the cauldron.
	 * @param sound The ID of the sound to play.
	 * @param volume The volume to play at.
	 * @param pitch The pitch to play at.
	 */
	public void playSound(String sound, float volume, float pitch) {
		context.getWorld().playSound(null, context.getPos(), Registry.SOUND_EVENT.get(new Identifier(sound)), SoundCategory.BLOCKS, volume, pitch);
	}

	/**
	 * Spawn an entity above the cauldron.
	 * @param entityType The ID of the type of entity to spawn.
	 */
	public void spawnEntity(String entityType) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		EntityType<?> type = Registry.ENTITY_TYPE.get(new Identifier(entityType));
		if (type.equals(EntityType.LIGHTNING_BOLT) && world instanceof ServerWorld) {
			LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
			lightning.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(pos));
			if (context.getPlayer() instanceof ServerPlayerEntity) {
				lightning.setChanneler((ServerPlayerEntity)context.getPlayer());
			}
			world.spawnEntity(lightning);
		} else {
			Entity entity = type.create(world);
			if (entity == null) return;
			if (type.getSpawnGroup() == SpawnGroup.MONSTER) {
				((MobEntity)entity).initialize((ServerWorldAccess) world, world.getLocalDifficulty(pos), SpawnReason.EVENT, null, null);
			}
			entity.setPos(pos.getX()+0.5, pos.getY()+1, pos.getZ()+0.5);
			world.spawnEntity(entity);
		}
	}
}
