package io.github.cottonmc.cotton.datapack.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class CrushingRecipe extends ProcessingRecipe {

	public CrushingRecipe(Identifier id, Ingredient input, ItemStack output, float exp, int processTime, Identifier bonusLootTable) {
		super(id, input, output, exp, processTime, bonusLootTable);
	}

	@Override
	public RecipeType<?> getType() {
		return CottonRecipes.CRUSHING_RECIPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CottonRecipes.CRUSHING_SERIALIZER;
	}
}
