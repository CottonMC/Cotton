package io.github.cottonmc.cotton.datapack.mixins;

import io.github.cottonmc.cotton.datapack.virtual.VirtualResourcePackManager;
import net.minecraft.client.resource.ClientBuiltinResourcePackProvider;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

/*
 Virtual resource packs are injected here to get them in before scanning,
 as Mixin doesn't support injecting into a constructor @At(INVOKE).
 */
@Mixin(ClientBuiltinResourcePackProvider.class)
public class MixinClientBuiltinResourcePackProvider {
    @Inject(method = "register", at = @At("RETURN"))
    private void addVirtualPacks(
            Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory, CallbackInfo info
    ) {
        VirtualResourcePackManager.INSTANCE
                .getCreatorForType(ResourceType.CLIENT_RESOURCES)
                .register(consumer, factory);
    }
}
