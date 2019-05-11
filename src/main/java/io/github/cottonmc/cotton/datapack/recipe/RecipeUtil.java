package io.github.cottonmc.cotton.datapack.recipe;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.config.CottonConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class RecipeUtil {
	
	private final static Set<String> removalsByIdentifier = new HashSet<>();
	private final static List<Predicate<Recipe<?>>> recipesForRemoval = new ArrayList<>();
	
	public static void init(CottonConfig config) {
		for(String idString : config.removeRecipesByIdentifier) {
			removalsByIdentifier.add(idString);
		}
		
		for(ItemStack stack : config.removeRecipesByItem) {
			removeRecipeFor(stack);
		}
	}
	
	
	/** Marks a recipe to block from RecipeManager. This must be done before resource load! */
	public static void removeRecipe(Identifier id) {
		recipesForRemoval.add(new IdentifierRemovalPredicate(id));
	}
	
	public static void removeRecipeFor(ItemStack product) {
		recipesForRemoval.add(new ProductRemovalPredicate(product));
	}
	
	public static Iterable<Predicate<Recipe<?>>> getRecipesForRemoval() {
		return recipesForRemoval;
	}
	
	public static Set<String> getIdentifiersForRemoval() {
		return removalsByIdentifier;
	}
	
	private static class IdentifierRemovalPredicate implements Predicate<Recipe<?>> {
		private final Identifier id;
		private IdentifierRemovalPredicate(Identifier id) {
			this.id = id;
		}
		
		@Override
		public boolean test(Recipe<?> t) {
			return t.getId().equals(id);
		}
	}
	
	private static class ProductRemovalPredicate implements Predicate<Recipe<?>> {
		private final ItemStack product;
		public ProductRemovalPredicate(ItemStack stack) {
			this.product = stack;
		}
		
		@Override
		public boolean test(Recipe<?> t) {
			return ItemStack.areEqual(t.getOutput(), product);
		}
	}
}
