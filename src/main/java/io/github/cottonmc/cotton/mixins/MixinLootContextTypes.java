package io.github.cottonmc.cotton.mixins;

import com.google.common.collect.BiMap;
import io.github.cottonmc.cotton.loot.CottonLootContextTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.loot.context.LootContextType;
import net.minecraft.world.loot.context.LootContextTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootContextTypes.class)
public abstract class MixinLootContextTypes {
	@Shadow @Final private static BiMap<Identifier, LootContextType> MAP;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void onClinit(CallbackInfo info) {
		MAP.put(new Identifier("cotton", "machine"), CottonLootContextTypes.MACHINE);
	}
}
