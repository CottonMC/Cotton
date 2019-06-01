package io.github.cottonmc.cotton.tweaker;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.cottonmc.cotton.Cotton;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * Various utilities for writing tweakers, due to the obfuscation of minecraft code.
 */
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

	/**
	 * Create an item stack from an item id.
	 * @param id The id of the item to get.
	 * @param amount The amount of the item in the stack.
	 * @return An item stack of the specified item and amount.
	 */
	public static ItemStack createItemStack(String id, int amount) {
		return new ItemStack(getItem(id), amount);
	}

	/**
	 * Create an item stack from an item.
	 * @param item The item to have a stack of.
	 * @param amount The amount of the item in the stack.
	 * @return An item stack of the specified item and amount.
	 */
	public static ItemStack createItemStack(Item item, int amount) {
		return new ItemStack(item, amount);
	}

	/**
	 * Add NBT to an item stack.
	 * @param stack The stack to add NBT to.
	 * @param nbt The string version of NBT to add.
	 * @return The stack with added NBT.
	 */
	public static ItemStack addNbtToStack(ItemStack stack, String nbt) {
		StringNbtReader reader = new StringNbtReader(new StringReader(nbt));
		try {
			CompoundTag tag = reader.parseCompoundTag();
			stack.setTag(tag);
		} catch (CommandSyntaxException e) {
			Cotton.logger.error("Error adding NBT to stack: " + e.getMessage());
		}
		return stack;
	}

	/**
	 * Get a potion of the specified type.
	 * Deprecated; use `getSpecialStack("minecraft:potion", id); instead
	 * @param id The id of the potion to get.
	 * @see <a href="https://minecraft.gamepedia.com/Potion#Data_values">Potion data values</a>
	 * @return an ItemStack of the desired potion, or an empty stack if the potion doesn't exist.
	 */
	@Deprecated
	public static ItemStack getPotion(String id) {
		Potion potion = Potion.byId(id);
		if (potion == Potions.EMPTY) return ItemStack.EMPTY;
		return PotionUtil.setPotion(new ItemStack(Items.POTION), potion);
	}

	/**
	 * Get a specal stack like a potion from its formatted getter string.
	 * @param getter The formatted getter string ([getter:id]->[entry:id]) to use.
	 * @return the gotten stack, or an empty stack if the getter or id doesn't exist
	 */
	public static ItemStack getSpecialStack(String getter) {
		String[] split = RecipeParser.processGetter(getter);
		return getSpecialStack(split[0], split[1]);
	}

	/**
	 * Get a special stack like a potion from its getter and ID.
	 * @param getter The id of the TweakerStackGetter to use.
	 * @param entry The id of the entry to get from the TweakerStackGetter.
	 * @return The gotten stack, or an empty stack if the getter or id doesn't exist.
	 */
	public static ItemStack getSpecialStack(String getter, String entry) {
		Identifier getterId = new Identifier(getter);
		Identifier itemId = new Identifier(entry);
		if (!TweakerStackGetter.GETTERS.containsKey(getterId)) return ItemStack.EMPTY;
		TweakerStackGetter get = TweakerStackGetter.GETTERS.get(getterId);
		return get.getSpecialStack(itemId);
	}

	/**
	 * Get an array of string ids for items in a given tag.
	 * @param tagId The id of the tag to get items for.
	 * @return An array of items in the tag.
	 */
	public static String[] getItemsInTag(String tagId) {
		Tag<Item> tag = ItemTags.getContainer().get(new Identifier(tagId));
		if (tag == null) return new String[0];
		Object[] items = tag.values().toArray();
		String[] res = new String[items.length];
		for (int i = 0; i < items.length; i++) {
			res[i] = Registry.ITEM.getId((Item)items[i]).toString();
		}
		return res;
	}
}
