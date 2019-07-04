package io.github.cottonmc.cotton.base.mixins;

import io.github.cottonmc.cotton.base.CottonInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public class MixinCottonInitializerServer {

	@Inject(method = "setupServer", at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;info(Ljava/lang/String;)V", ordinal = 0, remap = false))
	public void runCottonInit(CallbackInfoReturnable cir) {
		FabricLoader.getInstance().getEntrypoints("cotton", CottonInitializer.class).forEach(CottonInitializer::onCottonInit);
	}
}
