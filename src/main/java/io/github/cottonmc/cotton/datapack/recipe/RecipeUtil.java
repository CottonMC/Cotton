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

import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;

public class RecipeUtil {
	
	private final static Set<String> removalsByIdentifier = new HashSet<>();
	private final static List<Predicate<Recipe<?>>> recipesForRemoval = new ArrayList<>();
	
	public static void init(CottonConfig config) {
		//System.out.println("Removals: "+config.recipesToRemove);
		for(String idString : config.removeRecipesByIdentifier) {
			//Identifier id = new Identifier(idString);
			//Cotton.logger.info("Removing any recipe with identifier \""+idString+"\"");
			removalsByIdentifier.add(idString);
		}
		
		for(String itemIdString : config.removeRecipesByItem) {
			Identifier id = new Identifier(itemIdString);
			Item item = Registry.ITEM.getOrEmpty(id).orElse(Items.AIR);
			if (item!=Items.AIR) {
				
				//Cotton.logger.info("Removing any recipe resulting in Item "+ itemIdString);
				removeRecipeFor(new ItemStack(item));
			}
		}
	}
	
	
	/** Marks a recipe to block from RecipeManager. This must be done before resource load! */
	public static void removeRecipe(Identifier id) {
		recipesForRemoval.add(new IdentifierRemovalPredicate(id));
		/*
		File recipeFile = new File(getRecipeLocation(id), id.getPath() + ".json");
		try {
			if (!recipeFile.getParentFile().exists()) recipeFile.getParentFile().mkdirs();
			if (!recipeFile.exists()) recipeFile.createNewFile();
			String result = "{ \"type\": \"cotton:null\" }";
			FileOutputStream out = new FileOutputStream(recipeFile, false);
			out.write(result.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			Cotton.logger.warn("Failed to remove recipe " + id.toString() + ": " + e);
		}*/
	}
	
	public static void removeRecipeFor(ItemStack product) {
		recipesForRemoval.add(new ProductRemovalPredicate(product));
	}
	/*
	public static File getRecipeLocation(Identifier id) {
		return new File(Cotton.DATA_PACK_LOCATION, "data/" + id.getNamespace() + "/recipes");

	}*/
	
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
		System.out.println("Converting "+json+" into ItemStack");
		String itemIdString = json.get(String.class, "item");
		Item item = (Item)Registry.ITEM.getOrEmpty(new Identifier(itemIdString)).orElse(Items.AIR);
		ItemStack stack = new ItemStack(item);
		if (json.containsKey("count")) {
			Integer count = json.get(Integer.class, "count");
			if (count!=null) {
				stack.setAmount(count);
			}
		}
		System.out.println("Success: "+stack);
		return stack;
	}
	
	public static JsonObject saveItemStack(ItemStack stack) {
		System.out.println("Converting "+stack+" into Json");
		JsonObject result = new JsonObject();
		result.put("item", new JsonPrimitive(Registry.ITEM.getId(stack.getItem()).toString()));
		if (stack.getAmount()!=1) {
			result.put("count", new JsonPrimitive(stack.getAmount()));
		}
		System.out.println("SUCCESS: "+result);
		return result;
	}
}
