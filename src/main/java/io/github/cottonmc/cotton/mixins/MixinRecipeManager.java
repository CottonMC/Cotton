package io.github.cottonmc.cotton.mixins;

import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.cottonmc.cotton.datapack.recipe.RecipeUtil;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {
	
	@Inject(method="add", at=@At("HEAD"), cancellable = true)
	public void add(Recipe<?> recipe, CallbackInfo info) {
		for(Predicate<Recipe<?>> predicate : RecipeUtil.getRecipesForRemoval()) {
			if (predicate.test(recipe)) info.cancel();
		}
	}
}
