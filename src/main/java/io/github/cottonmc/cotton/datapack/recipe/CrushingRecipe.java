package io.github.cottonmc.cotton.datapack.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class CrushingRecipe extends CookingWithBonusRecipe {

	public CrushingRecipe(Identifier id, String group, Ingredient input, ItemStack output, float exp, int cookingTime, Identifier bonusLootTable) {
		super(CottonRecipes.CRUSHING_RECIPE, id, group, input, output, exp, cookingTime, bonusLootTable);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CottonRecipes.CRUSHING_SERIALIZER;
	}
}
