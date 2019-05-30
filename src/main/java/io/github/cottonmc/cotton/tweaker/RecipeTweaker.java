package io.github.cottonmc.cotton.tweaker;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.impl.ReloadListenersAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Map;

public class RecipeTweaker implements Tweaker {
	public static final RecipeTweaker INSTANCE = new RecipeTweaker();
	private RecipeManager manager;
	private int recipeCount;

	/**
	 * Used during data pack loading to set up recipe adding.
	 * DO NOT CALL THIS YOURSELF, EVER. IT WILL LIKELY MESS THINGS UP.
	 */
	@Override
	public void prepareReload(ResourceManager manager) {
		recipeCount = 0;
		if (manager instanceof ReloadListenersAccessor) {
			List<ResourceReloadListener> listeners = ((ReloadListenersAccessor)manager).cotton_getListeners();
			for (ResourceReloadListener listener : listeners) {
				if (listener instanceof RecipeManager) {
					this.manager = (RecipeManager)listener;
					return;
				}
			}
			throw new IllegalStateException("No recipe manager was found! Tweaker cannot register recipes!");
		}
	}

	/**
	 * Generate a recipe ID
	 * @param output The output stack of the recipe.
	 * @return A unique identifier for the recipe.
	 */
	public static Identifier getRecipeId(ItemStack output) {
		String resultName = Registry.ITEM.getId(output.getItem()).getPath();
		return new Identifier(Cotton.MODID, "tweaked/"+resultName+"-"+INSTANCE.recipeCount);
	}

	/**
	 * Register a recipe to the recipe manager.
	 * @param recipe A constructed recipe.
	 */
	public static void addRecipe(Recipe<?> recipe) {
		INSTANCE.recipeCount++;
		INSTANCE.manager.add(recipe);
	}

	public static void addShaped(String[] inputs, ItemStack output, int x, int y) {
		addShaped(inputs, output, x, y, "");
	}

	/**
	 * Register a shaped crafting recipe from an array of inputs.
	 * @param inputs The input item or tag ids required in order: left to right, top to bottom.
	 * @param output The output of the recipe.
	 * @param x How many columns the recipe needs.
	 * @param y How many rows the recipe needs.
	 * @param group The recipe group to go in, or "" for none.
	 */
	public static void addShaped(String[] inputs, ItemStack output, int x, int y, String group){
		Identifier recipeId = getRecipeId(output);
		DefaultedList<Ingredient> ingredients = DefaultedList.create();
			for (int i = 0; i < Math.min(inputs.length, x*y); i++) {
			String id = inputs[i];
			if (id.equals("")) continue;
			ingredients.add(i, RecipeParser.processIngredient(id));
		}
		addRecipe(new ShapedRecipe(recipeId, group, x, y, ingredients, output));
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
		Identifier recipeId = getRecipeId(output);
		try {
			pattern = RecipeParser.processPattern(pattern);
			Map<String, Ingredient> map = RecipeParser.processDictionary(dictionary);
			int x = pattern[0].length();
			int y = pattern.length;
			DefaultedList<Ingredient> ingredients = RecipeParser.getIngredients(pattern, map, x, y);
			addRecipe(new ShapedRecipe(recipeId, group, x, y, ingredients, output));
		} catch (TweakerSyntaxException e) {
			Cotton.logger.error("Error parsing shaped recipe - " + e.getMessage());
		}
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
		Identifier recipeId = getRecipeId(output);
		DefaultedList<Ingredient> ingredients = DefaultedList.create();
		for (int i = 0; i < Math.min(inputs.length, 9); i++) {
			String id = inputs[i];
			if (id.equals("")) continue;
			ingredients.add(i, RecipeParser.processIngredient(id));
		}
		addRecipe(new ShapelessRecipe(recipeId, group, output, ingredients));
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
		Identifier recipeId = getRecipeId(output);
		Ingredient ingredient = RecipeParser.processIngredient(input);
		addRecipe(new SmeltingRecipe(recipeId, group, ingredient, output, xp, cookTime));
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
		Identifier recipeId = getRecipeId(output);
		Ingredient ingredient = RecipeParser.processIngredient(input);
		addRecipe(new BlastingRecipe(recipeId, group, ingredient, output, xp, cookTime));
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
		Identifier recipeId = getRecipeId(output);
		Ingredient ingredient = RecipeParser.processIngredient(input);
		addRecipe(new SmokingRecipe(recipeId, group, ingredient, output, xp, cookTime));
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
		Identifier recipeId = getRecipeId(output);
		Ingredient ingredient = RecipeParser.processIngredient(input);
		addRecipe(new CampfireCookingRecipe(recipeId, group, ingredient, output, xp, cookTime));
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
		Identifier recipeId = getRecipeId(output);
		Ingredient ingredient = RecipeParser.processIngredient(input);
		addRecipe(new StonecuttingRecipe(recipeId, group, ingredient, output));
	}


}
