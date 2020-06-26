package io.github.cottonmc.cotton.cauldron;

import io.github.cottonmc.cotton.commons.CommonTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CauldronContext {
	private World world;
	private BlockPos pos;
	private BlockState state;
	private int level;
	private Fluid fluid;
	private DefaultedList<ItemStack> previousItems;
	private PlayerEntity player;
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
	public CauldronContext(World world, BlockPos pos, BlockState state, int level, Fluid fluid, DefaultedList<ItemStack> previousItems, PlayerEntity player, Hand hand, ItemStack stack) {
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

	/**
	 * @return The world the cauldron is in.
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * @return The position the cauldron is at.
	 */
	public BlockPos getPos() {
		return pos;
	}

	/**
	 * @return The blockstate of the cauldron.
	 */
	public BlockState getState() {
		return state;
	}

	/**
	 * @return How many bottles of fluid are in the cauldron.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return What fluid is currently in the cauldron.
	 */
	public Fluid getFluid() {
		return fluid;
	}

	/**
	 * @return Any items inserted into the cauldron before the behavior.
	 */
	public DefaultedList<ItemStack> getPreviousItems() {
		return previousItems;
	}

	/**
	 * @return The player interacting with the cauldron, or null if the interaction was automated.
	 */
	public PlayerEntity getPlayer() {
		return player;
	}

	/**
	 * @return The hand the player is interacting with the cauldron with, or null if there is no player.
	 */
	public Hand getHand() {
		return hand;
	}

	/**
	 * @return The item stack being used to interact with the cauldron.
	 */
	public ItemStack getStack() {
		return stack;
	}

	/**
	 * @return The {@link Cauldron} interface to interact with cauldron fluids in a standard way.
	 */
	public Cauldron getCauldron() {
		return (Cauldron)world.getBlockState(pos).getBlock();
	}

	/**
	 * Cauldrons might want to have fire underneath them, but there's more than one type of fire!
	 * Works based on the "cotton:cauldron_fire" tag.
	 * If a block in the tag (like the campfire) has the "lit" property, will only return true of lit is true.
	 * @return Whether there's currently a compatible fire under the cauldron.
	 */
	public boolean hasFireUnder() {
		BlockState state = world.getBlockState(pos.down());
		if (!CommonTags.CAULDRON_FIRE.contains(state.getBlock())) return false;
		else if (state.contains(Properties.LIT)) return state.get(Properties.LIT);
		else return true;
	}
}
