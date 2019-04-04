package io.github.cottonmc.cotton.cauldron;

import blue.endless.jankson.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CauldronContext {
	private World world;
	private BlockPos pos;
	private BlockState state;
	private int level;
	private Fluid fluid;
	private DefaultedList<ItemStack> previousItems;
	@Nullable
	private PlayerEntity player;
	@Nullable
	private Hand hand;
	private ItemStack stack;

	/**
	 * The context for an interaction with a cauldron.
	 * @param world world the cauldron's in
	 * @param pos the position of the cauldron
	 * @param state the current state of the cauldron
	 * @param level how many bottles (thirds of a bucket) of fluid the cauldron is holding
	 * @param fluid what fluid the cauldron is holding
	 * @param previousItems what previous items have been added to the cauldron since the last reaction
	 * @param player the player doing the interaction
	 * @param hand the hand the player's using
	 * @param stack the stack in the player's hand
	 */
	public CauldronContext(World world, BlockPos pos, BlockState state, int level, Fluid fluid, DefaultedList<ItemStack> previousItems, @Nullable PlayerEntity player, @Nullable Hand hand, ItemStack stack) {
		this.world = world;
		this.pos = pos;
		this.state = state;
		this.level = level;
		this.fluid = fluid;
		this.previousItems = previousItems;
		this.player = player;
		this.hand = hand;
		this.stack = stack;
	}

	public World getWorld() {
		return world;
	}

	public BlockPos getPos() {
		return pos;
	}

	public BlockState getState() {
		return state;
	}

	public int getLevel() {
		return level;
	}

	public Fluid getFluid() {
		return fluid;
	}

	public DefaultedList<ItemStack> getPreviousItems() {
		return previousItems;
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	public Hand getHand() {
		return hand;
	}

	public ItemStack getStack() {
		return stack;
	}
}
