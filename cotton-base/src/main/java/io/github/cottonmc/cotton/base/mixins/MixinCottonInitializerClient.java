package io.github.cottonmc.cotton.base.mixins;

import io.github.cottonmc.cotton.base.CottonInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MixinCottonInitializerClient {

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setPhase(Ljava/lang/String;)V", ordinal = 0))
	public void runCottonInit(CallbackInfo ci) {
		FabricLoader.getInstance().getEntrypoints("cotton", CottonInitializer.class).forEach(CottonInitializer::onCottonInit);
	}
}
