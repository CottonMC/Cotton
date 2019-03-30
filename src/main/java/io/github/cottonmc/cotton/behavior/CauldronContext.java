package io.github.cottonmc.cotton.behavior;

import blue.endless.jankson.annotation.Nullable;
import io.github.cottonmc.cotton.util.FluidProperty;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CauldronContext {
	private World world;
	private BlockPos pos;
	private BlockState state;
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
	 * @param player the player doing the interaction
	 * @param hand the hand the player's using
	 * @param stack the stack in the player's hand
	 */
	public CauldronContext(World world, BlockPos pos, BlockState state, @Nullable PlayerEntity player, @Nullable Hand hand, ItemStack stack) {
		this.world = world;
		this.pos = pos;
		this.state = state;
		this.player = player;
		this.hand = hand;
		this.stack = stack;
	}

	public int getCauldronLevel() {
		return state.get(CauldronBlock.LEVEL);
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

	public PlayerEntity getPlayer() {
		return player;
	}

	public Hand getHand() {
		return hand;
	}

	public ItemStack getStack() {
		return stack;
	}

	public Fluid getCauldronFluid() {
		return state.get(FluidProperty.ANY_FLUID).getFluid();
	}
}
