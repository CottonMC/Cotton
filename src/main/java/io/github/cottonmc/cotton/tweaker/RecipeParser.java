package io.github.cottonmc.cotton.tweaker;

import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Helper class to make public versions private recipe methods
 */
public class RecipeParser {

	/**
	 * Get an Ingredient from a string item or tag id.
	 * @param input The id to use, with a # at the front if it's a tag or & at the front if it's a potion.
	 * @return the Ingredient for the given id
	 */
	public static Ingredient processIngredient(String input) throws TweakerSyntaxException {
		if (input.indexOf('#') == 0) {
			String tag = input.substring(1);
			Tag<Item> itemTag = ItemTags.getContainer().get(new Identifier(tag));
			if (itemTag == null) throw new TweakerSyntaxException("Failed to get item tag for input: " + input);
			return Ingredient.fromTag(itemTag);
		} else if (input.indexOf('&') == 0) {
			ItemStack stack = TweakerUtils.getPotion(input.substring(1));
			if (stack.isEmpty()) throw new TweakerSyntaxException("Failed to get potion for input: " + input);
			return Ingredient.ofStacks(stack);
		} else {
			Item item = TweakerUtils.getItem(input);
			if (item == Items.AIR) throw new TweakerSyntaxException("Failed to get item for input: " + input);
			return Ingredient.ofItems(item);
		}
	}

	/**
	 * validate and parse a recipe pattern.
	 * @param pattern up to three strings of up to three characters each for the pattern
	 * @return processed pattern
	 */
	public static String[] processPattern(String... pattern) throws TweakerSyntaxException {
		if (pattern.length > 3) {
			throw new TweakerSyntaxException("Invalid pattern: too many rows, 3 is maximum");
		} else if (pattern.length == 0) {
			throw new TweakerSyntaxException("Invalid pattern: empty pattern not allowed");
		} else {
			for (int i = 0; i < pattern.length; i++) {
				String row = pattern[i];
				if (row.length() > 3) {
					throw new TweakerSyntaxException("Invalid pattern: too many columns, 3 is maximum");
				}

				if (i > 0 && pattern[0].length() != row.length()) {
					throw new TweakerSyntaxException("Invalid pattern: each row must be the same width");
				}

				pattern[i] = row;
			}
			int nextIndex = 2147483647;
			int highIndex = 0;
			int checked = 0;
			int sinceLastEmpty = 0;

			for (int i = 0; i < pattern.length; ++i) {
				String input = pattern[i];
				nextIndex = Math.min(nextIndex, findNextIngredient(input));
				int lastIndex = findNextIngredientReverse(input);
				highIndex = Math.max(highIndex, lastIndex);
				if (lastIndex < 0) {
					if (checked == i) {
						++checked;
					}

					++sinceLastEmpty;
				} else {
					sinceLastEmpty = 0;
				}
			}

			if (pattern.length == sinceLastEmpty) {
				return new String[0];
			} else {
				String[] combined = new String[pattern.length - sinceLastEmpty - checked];

				for (int i = 0; i < combined.length; ++i) {
					combined[i] = pattern[i + checked].substring(nextIndex, highIndex + 1);
				}

				return combined;
			}
		}
	}

	/**
	 * Process dictionaries into a Recipe-readable form.
	 * @param dictionary a map of keys to values for a recipe to parse.
	 * @return A map of string keys to ingredient values that a Recipe can read.
	 */
	public static Map<String, Ingredient> processDictionary(Map<String, String> dictionary) throws TweakerSyntaxException {
		Map<String, Ingredient> map = new HashMap<>();
		for (Map.Entry<String, String> entry : dictionary.entrySet()) {
			if (entry.getKey().length() != 1) {
				throw new TweakerSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
			}

			if (" ".equals(entry.getKey())) {
				throw new TweakerSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}
			map.put(entry.getKey(), processIngredient(entry.getValue()));
		}
		map.put(" ", Ingredient.EMPTY);
		return map;
	}

	/**
	 * Compile a pattern and dictionary into a full ingredient list.
	 * @param pattern A patern parsed by processPattern.
	 * @param dictionary A dictionary parsed by processDictionary.
	 * @param x How many columns there are.
	 * @param y How many rows there are.
	 * @return A defaulted list of ingredients.
	 */
	public static DefaultedList<Ingredient> getIngredients(String[] pattern, Map<String, Ingredient> dictionary, int x, int y) throws TweakerSyntaxException {
		DefaultedList<Ingredient> ingredients = DefaultedList.create(x * y, Ingredient.EMPTY);
		Set<String> keys = Sets.newHashSet(dictionary.keySet());
		keys.remove(" ");

		for(int i = 0; i < pattern.length; i++) {
			for(int j = 0; j < pattern[i].length(); j++) {
				String key = pattern[i].substring(j, j + 1);
				Ingredient ingredient = dictionary.get(key);
				if (ingredient == null) {
					throw new TweakerSyntaxException("Pattern references symbol '" + key + "' but it's not defined in the key");
				}

				keys.remove(key);
				ingredients.set(j + x * i, ingredient);
			}
		}

		if (!keys.isEmpty()) {
			throw new TweakerSyntaxException("Key defines symbols that aren't used in pattern: " + keys);
		} else {
			return ingredients;
		}
	}

	/**
	 * Skip forwards through a recipe row to find an ingredient key
	 * @param input a recipe row to parse
	 * @return index for the next ingredient character
	 */
	private static int findNextIngredient(String input) {
		int i;
		for (i = 0; i < input.length() && input.charAt(i) == ' '; i++) { }
		return i;
	}

	/**
	 * Skip backwards through a recipe row to find an ingredient key
	 * @param input a recipe row to parse
	 * @return index for the next ingredient character
	 */
	private static int findNextIngredientReverse(String input) {
		int i;
		for (i = input.length() - 1; i >= 0 && input.charAt(i) == ' '; i--) { }
		return i;
	}
}
