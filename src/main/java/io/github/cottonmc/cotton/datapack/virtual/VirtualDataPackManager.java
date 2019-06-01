package io.github.cottonmc.cotton.datapack.virtual;

import net.minecraft.resource.ResourcePackContainer;
import net.minecraft.resource.ResourcePackCreator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manages virtual data packs. See {@link #addPack(VirtualDataPack)} for registering packs.
 */
public enum VirtualDataPackManager implements ResourcePackCreator {
	INSTANCE;

	private final List<VirtualDataPack> packs = new ArrayList<>();

	@Override
	public <T extends ResourcePackContainer> void registerContainer(Map<String, T> map, ResourcePackContainer.Factory<T> factory) {
		int i = 0;
		for (VirtualDataPack pack : packs) {
			String id = pack.getId(i++);
			T container = ResourcePackContainer.of(id, false, () -> pack, factory, ResourcePackContainer.InsertionPosition.TOP);
			if (container != null) {
				map.put(id, container);
			}
		}
	}

	/**
	 * Adds a virtual data pack to the manager.
	 *
	 * @param pack the pack
	 */
	public void addPack(VirtualDataPack pack) {
		packs.add(pack);
	}
}
