package io.github.cottonmc.cotton.datapack.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class NullRecipe extends SpecialCraftingRecipe {
	public NullRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory craftingInventory, World world) {
		return false;
	}

	@Override
	public ItemStack craft(CraftingInventory craftingInventory) {
		return null;
	}

	@Override
	public boolean fits(int i, int i1) {
		return false;
	}

	@Override
	public RecipeSerializer<NullRecipe> getSerializer() {
		return CottonRecipes.NULL_SERIALIZER;
	}
}
