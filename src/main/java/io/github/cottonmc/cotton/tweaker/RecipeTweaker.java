package io.github.cottonmc.cotton.tweaker;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;

import java.util.Map;

/**
 * Use {@link io.github.cottonmc.libcd.tweaker.RecipeTweaker}
 */
@Deprecated
public class RecipeTweaker {

	/**
	 * Remove a recipe from the recipe manager.
	 * @param id The id of the recipe to remove.
	 */
	public static void removeRecipe(String id) {
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.removeRecipe(id);
	}

	/**
	 * Register a recipe to the recipe manager.
	 * @param recipe A constructed recipe.
	 */
	public static void addRecipe(Recipe<?> recipe) {
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.addRecipe(recipe);
	}

	/**
	 * Get a recipe ingredient from an item stack. Call this from java tweaker classes.
	 * @param stack The item stack to make an ingredient for.
	 * @return The wrapped ingredient of the stack.
	 */
	public static Ingredient ingredientForStack(ItemStack stack) {
		return Ingredient.ofStacks(stack);
	}

	public static void addShaped(String[][] inputs, ItemStack output) {
		addShaped(inputs, output, "");
	}

	/**
	 * Add a shaped recipe from a 2D array of inputs, like a standard CraftTweaker recipe.
	 * @param inputs the 2D array (array of arrays) of inputs to use.
	 * @param output The output of the recipe.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public static void addShaped(String[][] inputs, ItemStack output, String group) {
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.addShaped(inputs, output, group);
	}

	public static void addShaped(String[] inputs, ItemStack output, int width, int height) {
		addShaped(inputs, output, width, height, "");
	}

	/**
	 * Register a shaped crafting recipe from a 1D array of inputs.
	 * @param inputs The input item or tag ids required in order: left to right, top to bottom.
	 * @param output The output of the recipe.
	 * @param width How many rows the recipe needs.
	 * @param height How many columns the recipe needs.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public static void addShaped(String[] inputs, ItemStack output, int width, int height, String group){
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.addShaped(inputs, output, width, height, group);
	}

	public static void addShaped(String[] pattern, Map<String, String> dictionary, ItemStack output) {
		addShaped(pattern, dictionary, output, "");
	}

	/**
	 * Register a shaped crafting recipe from a pattern and dictionary.
	 * @param pattern A crafting pattern like one you'd find in a vanilla recipe JSON.
	 * @param dictionary A map of single characters to item or tag ids.
	 * @param output The output of the recipe.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public static void addShaped(String[] pattern, Map<String, String> dictionary, ItemStack output, String group) {
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.addShaped(pattern, dictionary, output, group);
	}

	public static void addShapeless(String[] inputs, ItemStack output) {
		addShapeless(inputs, output, "");
	}

	/**
	 * Register a shapeless crafting recipe from an array of inputs.
	 * @param inputs A list of input item or tag ids required for the recipe.
	 * @param output The output of the recipe.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public static void addShapeless(String[] inputs, ItemStack output, String group) {
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.addShapeless(inputs, output, group);
	}

	public static void addSmelting(String input, ItemStack output, int ticks, float xp) {
		addSmelting(input, output, ticks, xp, "");
	}

	/**
	 * Register a recipe to smelt in a standard furnace.
	 * @param input The input item or tag id.
	 * @param output The output of the recipe.
	 * @param cookTime How many ticks (1/20 of a second) to cook for. Standard value: 200
	 * @param xp How many experience points to drop per item, on average.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public static void addSmelting(String input, ItemStack output, int cookTime, float xp, String group) {
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.addSmelting(input, output, cookTime, xp, group);
	}

	public static void addBlasting(String input, ItemStack output, int ticks, float xp) {
		addBlasting(input, output, ticks, xp, "");
	}

	/**
	 * Register a recipe to smelt in a blast furnace.
	 * @param input The input item or tag id.
	 * @param output The output of the recipe.
	 * @param cookTime How many ticks (1/20 of a second) to cook for. Standard value: 100
	 * @param xp How many experience points to drop per item, on average.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public static void addBlasting(String input, ItemStack output, int cookTime, float xp, String group) {
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.addBlasting(input, output, cookTime, xp, group);
	}

	public static void addSmoking(String input, ItemStack output, int ticks, float xp) {
		addSmoking(input, output, ticks, xp, "");
	}

	/**
	 * Register a recipe to cook in a smoker.
	 * @param input The input item or tag id.
	 * @param output The output of the recipe.
	 * @param cookTime How many ticks (1/20 of a second) to cook for. Standard value: 100
	 * @param xp How many experience points to drop per item, on average.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public static void addSmoking(String input, ItemStack output, int cookTime, float xp, String group) {
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.addSmoking(input, output, cookTime, xp, group);
	}

	public static void addCampfire(String input, ItemStack output, int ticks, float xp) {
		addCampfire(input, output, ticks, xp, "");
	}

	/**
	 * Register a recipe to cook on a campfire.
	 * @param input The input item or tag id.
	 * @param output The output of the recipe.
	 * @param cookTime How many ticks (1/20 of a second) to cook for. Standard value: 600
	 * @param xp How many experience points to drop per item, on average.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public static void addCampfire(String input, ItemStack output, int cookTime, float xp, String group) {
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.addCampfire(input, output, cookTime, xp, group);
	}

	public static void addStonecutting(String input, ItemStack output) {
		addStonecutting(input, output, "");
	}

	/**
	 * Register a recipe to cut in the stonecutter.
	 * @param input The input item or tag id.
	 * @param output The output of the recipe.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public static void addStonecutting(String input, ItemStack output, String group) {
		io.github.cottonmc.libcd.tweaker.RecipeTweaker.addStonecutting(input, output, group);
	}

}
