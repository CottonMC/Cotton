package io.github.cottonmc.cotton.tweaker;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.impl.RecipeMapAccessor;
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
	private int removeCount;

	/**
	 * Used during data pack loading to set up recipe adding.
	 * DO NOT CALL THIS YOURSELF, EVER. IT WILL LIKELY MESS THINGS UP.
	 */
	@Override
	public void prepareReload(ResourceManager manager) {
		recipeCount = 0;
		removeCount = 0;
		if (manager instanceof ReloadListenersAccessor) {
			List<ResourceReloadListener> listeners = ((ReloadListenersAccessor)manager).cotton_getListeners();
			for (ResourceReloadListener listener : listeners) {
				if (listener instanceof RecipeManager) {
					this.manager = (RecipeManager)listener;
					return;
				}
			}
			Cotton.logger.error("No recipe manager was found! Tweaker cannot register recipes!");
			throw new IllegalStateException("No recipe manager was found! Tweaker cannot register recipes!");
		}
		Cotton.logger.error("No reload listeners accessor found! Tweaker cannot register recipes!");
		throw new IllegalStateException("No reload listeners accessor found! Tweaker cannot register recipes!");
	}

	@Override
	public String getApplyMessage() {
		return recipeCount + " " + (recipeCount == 1? "recipe" : "recipes" + " (" + removeCount + " removed)");
	}

	/**
	 * Generate a recipe ID. Call this from Java tweaker classes.
	 * @param output The output stack of the recipe.
	 * @return A unique identifier for the recipe.
	 */
	public static Identifier getRecipeId(ItemStack output) {
		String resultName = Registry.ITEM.getId(output.getItem()).getPath();
		return new Identifier(Cotton.MODID, "tweaked/"+resultName+"-"+INSTANCE.recipeCount);
	}

	public static void removeRecipe(String id) {
		Identifier recipeId = new Identifier(id);
		Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipeMap = ((RecipeMapAccessor)INSTANCE.manager).getRecipeMap();
		for (RecipeType<?> type : recipeMap.keySet()) {
			Map<Identifier, Recipe<?>> map = recipeMap.get(type);
			if (map.containsKey(recipeId)) {
				map.remove(recipeId);
				INSTANCE.removeCount++;
				return;
			}
		}
		Cotton.logger.error("Could not find recipe to remove: " + id);
	}

	/**
	 * Register a recipe to the recipe manager.
	 * @param recipe A constructed recipe.
	 */
	public static void addRecipe(Recipe<?> recipe) {
		INSTANCE.recipeCount++;
		try {
			INSTANCE.manager.add(recipe);
		} catch (Exception e) {
			Cotton.logger.error("Failed to add recipe from tweaker - " + e.getMessage());
		}
	}

	/**
	 * Get a recipe from an item stack. Call this from java tweaker classes.
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
		try {
			String[] processed = RecipeParser.processGrid(inputs);
			int width = inputs[0].length;
			int height = inputs.length;
			addShaped(processed, output, width, height, group);
		} catch (Exception e) {
			Cotton.logger.error("Error parsing shaped recipe - " + e.getMessage());
		}
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
		Identifier recipeId = getRecipeId(output);
		try {
			DefaultedList<Ingredient> ingredients = DefaultedList.create();
			for (int i = 0; i < Math.min(inputs.length, width * height); i++) {
				String id = inputs[i];
				if (id.equals("")) continue;
				ingredients.add(i, RecipeParser.processIngredient(id));
			}
			addRecipe(new ShapedRecipe(recipeId, group, width, height, ingredients, output));
		} catch (Exception e) {
			Cotton.logger.error("Error parsing shaped recipe - " + e.getMessage());
		}
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
		} catch (Exception e) {
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
		try {
		DefaultedList<Ingredient> ingredients = DefaultedList.create();
		for (int i = 0; i < Math.min(inputs.length, 9); i++) {
			String id = inputs[i];
			if (id.equals("")) continue;
			ingredients.add(i, RecipeParser.processIngredient(id));
		}
		addRecipe(new ShapelessRecipe(recipeId, group, output, ingredients));
		} catch (Exception e) {
			Cotton.logger.error("Error parsing shapeless recipe - " + e.getMessage());
		}
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
		try {
			Ingredient ingredient = RecipeParser.processIngredient(input);
			addRecipe(new SmeltingRecipe(recipeId, group, ingredient, output, xp, cookTime));
		} catch (Exception e) {
			Cotton.logger.error("Error parsing smelting recipe - " + e.getMessage());
		}
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
		try {
		Ingredient ingredient = RecipeParser.processIngredient(input);
		addRecipe(new BlastingRecipe(recipeId, group, ingredient, output, xp, cookTime));
		} catch (Exception e) {
			Cotton.logger.error("Error parsing blasting recipe - " + e.getMessage());
		}
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
		try {
			Ingredient ingredient = RecipeParser.processIngredient(input);
			addRecipe(new SmokingRecipe(recipeId, group, ingredient, output, xp, cookTime));
		} catch (Exception e) {
			Cotton.logger.error("Error parsing smokig recipe - " + e.getMessage());
		}
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
		try {
		Ingredient ingredient = RecipeParser.processIngredient(input);
		addRecipe(new CampfireCookingRecipe(recipeId, group, ingredient, output, xp, cookTime));
		} catch (Exception e) {
			Cotton.logger.error("Error parsing campfire recipe - " + e.getMessage());
		}
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
		try {
			Ingredient ingredient = RecipeParser.processIngredient(input);
			addRecipe(new StonecuttingRecipe(recipeId, group, ingredient, output));
		} catch (Exception e) {
			Cotton.logger.error("Error parsing stonecutter recipe - " + e.getMessage());
		}
	}


}
