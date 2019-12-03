package io.github.cottonmc.cotton.cauldron;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface Cauldron {

	/**
	 * Attempt to fill a cauldron.
	 * @param world The world the cauldron is in.
	 * @param pos The position the cauldron is at.
	 * @param state The current state of the cauldron to fill.
	 * @param fluid The fluid to fill a cauldron with.
	 * @param bottles How many bottles (1/3 of a bucket) to fill the cauldron with.
	 * @return Whether the fill was successful.
	 */
	boolean fill(World world, BlockPos pos, BlockState state, Fluid fluid, int bottles);

	/**
	 * Attempt to drain a cauldron.
	 * @param world The world the cauldron is in.
	 * @param pos The position the cauldron is at.
	 * @param state The current state of the cauldron to drain.
	 * @param fluid The fluid to drain from a cauldron.
	 * @param bottles How many bottles (1/3 of a bucket) to drain from the cauldron.
	 * @return Whether the drain was successful.
	 */
	boolean drain(World world, BlockPos pos, BlockState state, Fluid fluid, int bottles);

	/**
	 * Query whether a given cauldron can currently accept a certain fluid.
	 * @param world The world the cauldron is in.
	 * @param pos The position the cauldron is at.
	 * @param state The current state of the cauldron to query.
	 * @param fluid The fluid to query about.
	 * @return Whether the cauldron will accept that fluid currently.
	 */
	boolean canAcceptFluid(World world, BlockPos pos, BlockState state, Fluid fluid);

	/**
	 * Populate a cauldron context with information about fluid state and previous items.
	 * @param world The world the cauldron is in.
	 * @param pos The position the cauldro is at.
	 * @param player The player activating the behavior, or null if there is no player.
	 * @param stack The ItemStack used in the context.
	 * @return The fully-populated context.
	 */
	CauldronContext createContext(World world, BlockPos pos, PlayerEntity player, ItemStack stack);
}
