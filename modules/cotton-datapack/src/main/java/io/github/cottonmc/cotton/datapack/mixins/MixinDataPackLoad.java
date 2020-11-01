package io.github.cottonmc.cotton.datapack.mixins;

import io.github.cottonmc.cotton.datapack.GlobalResourcePackProvider;
import io.github.cottonmc.cotton.datapack.virtual.VirtualResourcePackManager;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MixinDataPackLoad {


	@Inject(method = "loadDataPacks", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePackManager;scanPacks()V"))
	private static void addGlobalDataPacks(ResourcePackManager resourcePackManager, DataPackSettings dataPackSettings, boolean safeMode, CallbackInfoReturnable<DataPackSettings> info) {
		((ResourcePackManagerAccessor)resourcePackManager).getProviders().add(new GlobalResourcePackProvider());
		((ResourcePackManagerAccessor)resourcePackManager).getProviders().add(VirtualResourcePackManager.INSTANCE.getCreatorForType(ResourceType.SERVER_DATA));
	}
}
