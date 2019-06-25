package io.github.cottonmc.cotton.mixins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import net.minecraft.recipe.RecipeType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.datapack.recipe.RecipeUtil;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {
	@ModifyVariable(method = "method_20705", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;", ordinal = 0, remap = false))
	private Iterator<Map.Entry<Identifier, JsonObject>> filterIterator(Iterator<Map.Entry<Identifier, JsonObject>> iterator) {
		ArrayList<Map.Entry<Identifier, JsonObject>> replacement = new ArrayList<>();
		while(iterator.hasNext()) {
			Map.Entry<Identifier, JsonObject> cur = iterator.next();
			Identifier recipeId = cur.getKey();
			
			if (RecipeUtil.getIdentifiersForRemoval().contains(recipeId.toString())) {
				Cotton.logger.info("Blocking recipe by identifier: "+recipeId);
			} else {
				replacement.add(cur);
			}
		}
		
		return replacement.iterator();
	}

	// TODO: Should this be also done in the client-side method_20702 (setRecipes?)
	@Redirect(method = "method_20705", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap$Builder;put(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;", remap = false))
	private ImmutableMap.Builder<Identifier, Recipe<?>> onPutRecipe(ImmutableMap.Builder<Identifier, Recipe<?>> builder, Object key, Object value) {
		Identifier id = (Identifier) key;
		Recipe<?> recipe = (Recipe<?>) value;

		for (Predicate<Recipe<?>> predicate : RecipeUtil.getRecipesForRemoval()) {
			if (predicate.test(recipe)) {
				Cotton.logger.info("Blocked recipe by predicate: "+recipe.getId());
				return builder;
			}
		}

		return builder.put(id, recipe);
	}

}
