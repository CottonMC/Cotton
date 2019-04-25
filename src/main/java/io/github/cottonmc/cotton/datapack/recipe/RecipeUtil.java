package io.github.cottonmc.cotton.datapack.recipe;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.config.CottonConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import blue.endless.jankson.JsonObject;

public class RecipeUtil {
	
	private final static List<Predicate<Recipe<?>>> recipesForRemoval = new ArrayList<>();
	
	public static void init(CottonConfig config) {
		System.out.println("Removals: "+config.recipesToRemove);
		for(JsonObject s : config.recipesToRemove) {
			if (s.containsKey("identifier")) {
				String elem = s.get(String.class, "identifier");
				if (elem!=null && !elem.isEmpty()) {
					Identifier id = new Identifier(elem);
					Cotton.logger.info("Removing any recipe with identifier \""+id+"\"");
					removeRecipe(id);
				} else {
					Cotton.logger.error("Can't parse identifier for "+s.toJson());
				}
				
			} else if (s.containsKey("item")) {
				String elem = s.get(String.class, "item");
				if (elem!=null && !elem.isEmpty()) {
					Identifier id = new Identifier(elem);
				
					if (Registry.ITEM.containsId(id)) {
						ItemStack stack = new ItemStack(Registry.ITEM.get(id));
						if (s.containsKey("amount")) {
							Integer amount = s.get(Integer.class, "amount");
							if (amount!=null && amount>0) {
								stack.setAmount(amount);
							} else {
								Cotton.logger.error("Couldn't parse amount for item \""+id+"\". Using 1x instead.");
							}
						}
						
						Cotton.logger.info("removing any recipe that creates "+stack);
						removeRecipeFor(stack);
					}
				} else {
					Cotton.logger.error("Can't parse item Identifier for "+s);
				}
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
