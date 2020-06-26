package io.github.cottonmc.cotton.datapack.mixins;

import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ResourcePackManager.class)
public interface ResourcePackManagerAccessor {
	@Accessor
	Set<ResourcePackProvider> getProviders();
}
