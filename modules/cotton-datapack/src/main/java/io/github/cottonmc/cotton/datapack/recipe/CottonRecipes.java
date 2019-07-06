package io.github.cottonmc.cotton.datapack.recipe;

import io.github.cottonmc.cotton.datapack.CottonDatapack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CottonRecipes {
	public static final RecipeType<NullRecipe> NULL_RECIPE = register("null");
	public static final RecipeSerializer<NullRecipe> NULL_SERIALIZER = register("null", new SpecialRecipeSerializer<>(NullRecipe::new));

	public static <T extends Recipe<?>> RecipeType<T> register(String id) {
		return Registry.register(Registry.RECIPE_TYPE, new Identifier(CottonDatapack.SHARED_NAMESPACE, id), new RecipeType<T>() {
			public String toString() {
				return id;
			}
		});
	}

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String name, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(CottonDatapack.SHARED_NAMESPACE, name), serializer);
	}

	public static void init() {

	}
}
