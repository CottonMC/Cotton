package io.github.cottonmc.cotton.mixins;

import io.github.cottonmc.cotton.CottonInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Bootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class MixinCottonInitialize {

	@Inject(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FireBlock;registerDefaultFlammables()V"))
	private static void initCottonEntrypoint(CallbackInfo ci) {
		FabricLoader.getInstance().getEntrypoints("cotton", CottonInitializer.class).forEach((CottonInitializer::initialize));
	}
}
