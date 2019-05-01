package io.github.cottonmc.cotton.datapack.recipe;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.config.CottonConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;

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
	
	public static ItemStack getItemStack(JsonObject json) {
		String itemIdString = json.get(String.class, "item");
		Item item = (Item)Registry.ITEM.getOrEmpty(new Identifier(itemIdString)).orElse(Items.AIR);
		ItemStack stack = new ItemStack(item);
		if (json.containsKey("count")) {
			Integer count = json.get(Integer.class, "count");
			if (count!=null) {
				stack.setAmount(count);
			}
		}
		return stack;
	}
	
	public static ItemStack getItemStackPrimitive(Object obj) {
		String itemIdString = obj.toString();
		Item item = (Item)Registry.ITEM.getOrEmpty(new Identifier(itemIdString)).orElse(Items.AIR);
		ItemStack stack = new ItemStack(item);
		return stack;
	}
	
	public static JsonElement saveItemStack(ItemStack stack) {
		JsonPrimitive id = new JsonPrimitive(Registry.ITEM.getId(stack.getItem()).toString());
		if (stack.getAmount()==1) return id;
	
		JsonObject result = new JsonObject();
		result.put("item", new JsonPrimitive(Registry.ITEM.getId(stack.getItem()).toString()));
		result.put("count", new JsonPrimitive(stack.getAmount()));
		return result;
		
	}
}
