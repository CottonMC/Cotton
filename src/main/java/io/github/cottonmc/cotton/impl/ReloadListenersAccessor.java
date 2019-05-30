package io.github.cottonmc.cotton.impl;

import net.minecraft.resource.ResourceReloadListener;

import java.util.List;

public interface ReloadListenersAccessor {
	 List<ResourceReloadListener> cotton_getListeners();
}
