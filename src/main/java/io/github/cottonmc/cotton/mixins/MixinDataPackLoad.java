package io.github.cottonmc.cotton.mixins;

import io.github.cottonmc.cotton.datapack.GlobalResourcePackCreator;
import net.minecraft.resource.ResourcePackContainer;
import net.minecraft.resource.ResourcePackContainerManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(MinecraftServer.class)
public class MixinDataPackLoad {

	@Shadow
	private ResourcePackContainerManager<ResourcePackContainer> resourcePackContainerManager;

	@Inject(method = "method_3800",
			at = @At(value = "HEAD"))
		public void method_3800(File file, LevelProperties properties, CallbackInfo info) {
		resourcePackContainerManager.addCreator(new GlobalResourcePackCreator());
	}
}