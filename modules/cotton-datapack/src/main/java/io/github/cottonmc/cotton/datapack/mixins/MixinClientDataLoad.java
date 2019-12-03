package io.github.cottonmc.cotton.datapack.mixins;

import io.github.cottonmc.cotton.datapack.virtual.VirtualResourcePackManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.resource.ClientResourcePackProfile;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourceType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinClientDataLoad {
	@Shadow @Final private ResourcePackManager<ClientResourcePackProfile> resourcePackManager;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onConstruct(RunArgs runArgs, CallbackInfo info) {
		resourcePackManager.registerProvider(VirtualResourcePackManager.INSTANCE.getCreatorForType(ResourceType.CLIENT_RESOURCES));
	}
}
