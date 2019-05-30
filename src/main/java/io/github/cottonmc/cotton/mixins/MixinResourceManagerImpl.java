package io.github.cottonmc.cotton.mixins;

import io.github.cottonmc.cotton.impl.ReloadListenersAccessor;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourceReloadListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ReloadableResourceManagerImpl.class)
public class MixinResourceManagerImpl implements ReloadListenersAccessor {

	@Shadow @Final private List<ResourceReloadListener> listeners;

	@Override
	public List<ResourceReloadListener> cotton_getListeners() {
		return listeners;
	}
}
