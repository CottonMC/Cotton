package io.github.cottonmc.cotton.datapack.mixins;

import io.github.cottonmc.cotton.datapack.GlobalResourcePackProvider;
import io.github.cottonmc.cotton.datapack.virtual.VirtualResourcePackManager;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(MinecraftServer.class)
public class MixinDataPackLoad {

	@Shadow
	@Final
	private ResourcePackManager<ResourcePackProfile> dataPackManager;

	@Inject(method = "loadWorldDataPacks", at = @At(value = "HEAD"))
	public void addGlobalDataPacks(File file, LevelProperties properties, CallbackInfo info) {
		dataPackManager.registerProvider(new GlobalResourcePackProvider());
		dataPackManager.registerProvider(VirtualResourcePackManager.INSTANCE.getCreatorForType(ResourceType.SERVER_DATA));
	}
}