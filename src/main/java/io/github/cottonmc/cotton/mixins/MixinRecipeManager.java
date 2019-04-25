package io.github.cottonmc.cotton.mixins;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.datapack.recipe.RecipeUtil;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {
	
	/*
	 //Would love to do this but there's just no good documentation on HOW to use the captured locals.
	@Inject(method="apply", at=@At(value="INVOKE", target="net.minecraft.resource.ResourceManager.getResource(Lnet.minecraft.util.Identifier;)", ordinal=1), locals=LocalCapture.CAPTURE_FAILHARD)
	public void filterApply(ResourceManager resourceManager, CallbackInfo info) {
		System.out.println("NOW HOW DO I GET THE IDENTIFIER");
		
	}*/
	
	
	@Shadow
	private static final Logger LOGGER = LogManager.getLogger();
	@Shadow
	public static final int PREFIX_LENGTH = "recipes/".length();
	@Shadow
	public static final int SUFFIX_LENGTH = ".json".length();
	@Shadow
	private final Map<RecipeType<?>, Map<Identifier, Recipe<?>>> recipeMap = new HashMap<>();
	@Shadow
	private boolean hadErrors;
	
	@Shadow
	public static Recipe<?> deserialize(Identifier identifier_1, JsonObject jsonObject_1) { return null; }
	@Shadow
	public void add(Recipe<?> recipe) {}
	@Shadow
	private static void clear(Map<RecipeType<?>, Map<Identifier, Recipe<?>>> map) {}
	
	//Overwrites `apply` completely. Sorry folks. On the bright side, I cleaned the code presentation up a little.
	public void apply(ResourceManager resourceManager) {
		Gson gson = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
		this.hadErrors = false;
		clear(this.recipeMap);
		Iterator<Identifier> resources = resourceManager.findResources("recipes", (s) -> s.endsWith(".json")).iterator();

		while(resources.hasNext()) {
			Identifier resourceFile = (Identifier)resources.next();
			String path = resourceFile.getPath();
			Identifier resourceId = new Identifier(resourceFile.getNamespace(), path.substring(PREFIX_LENGTH, path.length() - SUFFIX_LENGTH));
			
			/* COTTON CHANGES */
			System.out.println("Testing "+resourceId.toString()+" against "+RecipeUtil.getIdentifiersForRemoval());
			if (RecipeUtil.getIdentifiersForRemoval().contains(resourceId.toString())) {
				Cotton.logger.info("Blocked recipe "+resourceId);
				continue;
			}
			/* END COTTON CHANGES */
			
			try {
				Resource resource = resourceManager.getResource(resourceFile);
				Throwable error = null;

				try {
					JsonObject recipeObject = (JsonObject)JsonHelper.deserialize(gson, IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8), JsonObject.class);
					if (recipeObject == null) {
						LOGGER.error("Couldn't load recipe {} as it's null or empty", resourceId);
					} else {
						this.add(deserialize(resourceId, recipeObject));
					}
				} catch (Throwable t) {
					/* COTTON CHANGES */
					if (resourceId.getNamespace().equals("c")) {
						//TODO: This simplifies errors in our namespace, we may consider eliminating them.
						LOGGER.error("Couldn't deserialize recipe \"{}\": {}", resourceId, t.getLocalizedMessage());
						continue;
					}
					/* END COTTON CHANGES */
					error = t;
					throw t;
				} finally {
					if (resource != null) {
						if (error != null) {
							try {
								resource.close();
							} catch (Throwable t) {
								error.addSuppressed(t);
							}
						} else {
							resource.close();
						}
					}

				}
			} catch (IllegalArgumentException | JsonParseException ex) {
				LOGGER.error("Parsing error loading recipe {}", resourceId, ex);
				this.hadErrors = true;
			} catch (IOException ex2) {
				LOGGER.error("Couldn't read custom advancement {} from {}", resourceId, resourceFile, ex2);
				this.hadErrors = true;
			}
		}

		LOGGER.info("Loaded {} recipes", this.recipeMap.size());
	}
	
	@Inject(method="add", at=@At("HEAD"), cancellable = true)
	public void mixinAdd(Recipe<?> recipe, CallbackInfo info) {
		for(Predicate<Recipe<?>> predicate : RecipeUtil.getRecipesForRemoval()) {
			if (predicate.test(recipe)) {
				Cotton.logger.info("Blocked recipe "+recipe.getId()+" by predicate.");
				info.cancel();
			}
		}
	}
}
