package io.github.cottonmc.cotton.tweaker;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class TweakerUtils {
	/**
	 * Get a registered item inside a script.
	 * @param id The id to search for.
	 * @return The registered item, or Items.AIR if it doesn't exist.
	 */
	public static Item getItem(String id) {
		return Registry.ITEM.get(new Identifier(id));
	}

	/**
	 * Get the item from an item stack.
	 * @param stack The stack to check.
	 * @return The item of the stack.
	 */
	public static Item getStackItem(ItemStack stack) {
		return stack.getItem();
	}

	/**
	 * Get a registered block inside a script.
	 * @param id The id to search for.
	 * @return The registered item, or Blocks.AIR if it doesn't exist.
	 */
	public static Block getBlock(String id) {
		return Registry.BLOCK.get(new Identifier(id));
	}

	/**
	 * Get a registered fluid inside a script.
	 * @param id The id to search for.
	 * @return The registered fluid, or Fluids.EMPTY if it doesn't exist.
	 */
	public static Fluid getFluid(String id) {
		return Registry.FLUID.get(new Identifier(id));
	}

	/**
	 * Get a registered entity type inside a script.
	 * @param id The id to search for.
	 * @return The registered entity, or EntityType.PIG if it doesn't exist.
	 */
	public static EntityType getEntity(String id) {
		return Registry.ENTITY_TYPE.get(new Identifier(id));
	}

	/**
	 * Get a registered sound inside a script.
	 * @param id The id to search for.
	 * @return The registered sound, or SoundEvents.ENTITY_ITEM_PICKUP if it doesn't exist.
	 */
	public static SoundEvent getSound(String id) {
		return Registry.SOUND_EVENT.get(new Identifier(id));
	}

	/**
	 * Check if a DefaultedList (like the ones inventories use) is empty.
	 * Necessary because DefaultedList stays within Collection<E> spec for once.
	 * @param items The DefaultedList to check.
	 * @return Whether all the item stacks in the list are empty or not.
	 */
	public static boolean isItemListEmpty(DefaultedList<ItemStack> items) {
		for (ItemStack stack : items) {
			if (!stack.isEmpty()) return false;
		}
		return true;
	}
}
