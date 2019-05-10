package io.github.cottonmc.cotton.mixins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.datapack.recipe.RecipeUtil;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {
	@Shadow
	public static int PREFIX_LENGTH;
	@Shadow
	public static int SUFFIX_LENGTH;
	
	@ModifyVariable(method="apply", at=@At(value="INVOKE_ASSIGN", target="Ljava/util/Collection;iterator()Ljava/util/Iterator;", ordinal=0, remap=false))
	public Iterator<Identifier> filterIterator(Iterator<Identifier> iterator) {
		ArrayList<Identifier> replacement = new ArrayList<>();
		while(iterator.hasNext()) {
			Identifier cur = iterator.next();
			String string_1 = cur.getPath();
			Identifier recipeId = new Identifier(cur.getNamespace(), string_1.substring(PREFIX_LENGTH, string_1.length() - SUFFIX_LENGTH));
			
			if (RecipeUtil.getIdentifiersForRemoval().contains(recipeId.toString())) {
				Cotton.logger.info("Blocking recipe by identifier: "+recipeId);
			} else {
				replacement.add(cur);
			}
		}
		
		return replacement.iterator();
	}
	
	@Inject(method="add", at=@At("HEAD"), cancellable = true)
	public void mixinAdd(Recipe<?> recipe, CallbackInfo info) {
		for(Predicate<Recipe<?>> predicate : RecipeUtil.getRecipesForRemoval()) {
			if (predicate.test(recipe)) {
				Cotton.logger.info("Blocked recipe by predicate: "+recipe.getId());
				info.cancel();
			}
		}
	}
}
