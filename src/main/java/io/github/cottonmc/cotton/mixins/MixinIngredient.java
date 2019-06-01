package io.github.cottonmc.cotton.mixins;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import io.github.cottonmc.cotton.tweaker.TweakerUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Ingredient.class)
public class MixinIngredient {
	//TODO: uncomment once Fabric has a way to transform access of classes
//	@Inject(method = "entryFromJson", at = @At("HEAD"))
//	private static void verifyNoDupe(JsonObject json, CallbackInfoReturnable cir) {
//		if (json.has("getter") && (json.has("item") || json.has("tag"))) {
//			throw new JsonParseException("An ingredient entry is either a tag, an item, or a getter, not multiple");
//		}
//	}
//
//	@Inject(method = "entryFromJson", at = @At(value = "INVOKE", target = "Lcom/google/gson/JsonObject;has(Ljava/lang/String;)Z", ordinal = 2), cancellable = true)
//	private static void entryFromGetter(JsonObject json, CallbackInfoReturnable cir) {
//		if (json.has("getter")) {
//			String getter = JsonHelper.getString(json, "getter");
//			ItemStack stack = TweakerUtils.getSpecialStack(getter);
//			if (stack.isEmpty()) throw new JsonSyntaxException("Unknown getter '" + getter + "'");
//			cir.setReturnValue(new Ingredient.StackEntry(stack));
//		}
//	}
}
