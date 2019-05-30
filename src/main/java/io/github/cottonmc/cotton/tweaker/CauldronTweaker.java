package io.github.cottonmc.cotton.tweaker;

import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.cotton.cauldron.CauldronContext;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class CauldronTweaker implements Tweaker {
	public final Map<Predicate<CauldronContext>, CauldronBehavior> behaviors = new HashMap<>();
	public static final CauldronTweaker INSTANCE = new CauldronTweaker();

	/**
	 * Used during data pack loading to clear the behavior list.
	 * DO NOT CALL THIS YOURSELF, EVER. IT WILL WIPE ALL DATA PACK BEHAVIORS.
	 */
	public void prepareReload(ResourceManager manager) {
		INSTANCE.behaviors.clear();
	}

	/**
	 * Register a new cauldron behavior.
	 * Construct new classes and pass them functions for these. Each are passed a {@link CauldronContext}.
	 * @param ctx A predicate for under what conditions the behavior should be performed.
	 * @param behavior The behavior to perform when the conditions are met.
	 */
	public static void registerBehavior(Predicate<CauldronContext> ctx, CauldronBehavior behavior) {
		INSTANCE.behaviors.put(ctx, behavior);
	}

	/**
	 * Drain a cauldron of its fluid (for ease of calling from scripts).
	 * @param ctx The {@link CauldronContext} being used.
	 * @param bottles How many bottles of fluid to drain.
	 * @return Whether the drain was successful.
	 */
	public static boolean drainCauldron(CauldronContext ctx, int bottles) {
		return ctx.getCauldron().drain(ctx.getWorld(), ctx.getPos(), ctx.getState(), ctx.getFluid(), bottles);
	}

	/**
	 * Fill a cauldron with a given fluid (for ease of calling from scripts).
	 * @param ctx The {@link CauldronContext} being used.
	 * @param fluid What fluid to fill with.
	 * @param bottles How many bottles of fluid to fill.
	 * @return Whether the fill was successful.
	 */
	public static boolean fillCaudron(CauldronContext ctx, Fluid fluid, int bottles) {
		return ctx.getCauldron().fill(ctx.getWorld(), ctx.getPos(), ctx.getState(), fluid, bottles);
	}

	/**
	 * Take items from the stack used in a cauldron behavior.
	 * @param ctx The {@link CauldronContext} being used.
	 * @param amount How many items to take.
	 * @return Whether the items could be taken.
	 */
	public static boolean takeItem(CauldronContext ctx, int amount) {
		if (ctx.getStack().getAmount() < amount) return false;
		if (ctx.getPlayer() == null || !ctx.getPlayer().abilities.creativeMode) ctx.getStack().subtractAmount(amount);
		return true;
	}

	/**
	 * Give items to a player using a cauldron behavior, or drop them on the ground if there's no player.
	 * Items dropped will have the "NoCauldronCollect" scoreboard tag, which prevents in-spec cauldrons from picking them up hopper-style.
	 * @param ctx The {@link CauldronContext} being used.
	 * @param item The item to give.
	 * @param amount How many of the item to give.
	 * @return Whether the items could be given.
	 */
	public static boolean giveItem(CauldronContext ctx, Item item, int amount) {
		if (amount > item.getMaxAmount()) return false;
		PlayerEntity player = ctx.getPlayer();
		ItemStack give = new ItemStack(item, amount);
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
			ItemEntity entity = new ItemEntity(ctx.getWorld(), ctx.getPos().getX(), ctx.getPos().getY()+1, ctx.getPos().getZ(), give);
			entity.addScoreboardTag("NoCauldronCollect");
			ctx.getWorld().spawnEntity(entity);
		}
		return true;
	}

	/**
	 * Play a sound effect from the cauldron.
	 * @param ctx The {@link CauldronContext} being used.
	 * @param sound The sound to play.
	 * @param volume The volume to play at.
	 * @param pitch The pitch to play at.
	 */
	public static void playSound(CauldronContext ctx, SoundEvent sound, float volume, float pitch) {
		ctx.getWorld().playSound(null, ctx.getPos(), sound, SoundCategory.BLOCKS, volume, pitch);
	}

	/**
	 * Spawn an entity above the cauldron.
	 * @param ctx The {@link CauldronContext} being used.
	 * @param type The type of entity to spawn.
	 */
	public static void spawnEntity(CauldronContext ctx, EntityType type) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getPos();
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
