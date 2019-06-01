package io.github.cottonmc.cotton.tweaker;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

/**
 * Some ItemStacks, like vanilla potions, have entirely different functions and names based on NBT.
 * Because of that, it's hard to use those stacks in recipes.
 * CottonTweaker uses a "[getter id]->[entry id]" syntax to get those recipes
 */
public interface TweakerStackGetter {
	public static Map<Identifier, TweakerStackGetter> GETTERS = new HashMap<>();

	static void registerGetter(Identifier id, TweakerStackGetter getter) {
		GETTERS.put(id, getter);
	}

	/**
	 * Get an ItemStack from a registered processor
	 * @param entry The Identifier of the entry to get
	 * @return the proper ItemStack for the given Identifier, or an empty stack if the entry doesn't exist
	 */
	ItemStack getSpecialStack(Identifier entry);
}
