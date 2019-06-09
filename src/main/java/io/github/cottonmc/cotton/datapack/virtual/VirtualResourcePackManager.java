package io.github.cottonmc.cotton.datapack.virtual;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.resource.ResourcePackContainer;
import net.minecraft.resource.ResourcePackCreator;
import net.minecraft.resource.ResourceType;

import java.util.*;

/**
 * Manages virtual resource packs. See {@link #addPack(VirtualResourcePack, Collection)} for registering packs.
 */
public enum VirtualResourcePackManager {
	INSTANCE;

	private final Multimap<ResourceType, VirtualResourcePack> packs = MultimapBuilder.hashKeys().arrayListValues().build();

	public ResourcePackCreator getCreatorForType(ResourceType type) {
		return new ResourcePackCreator() {
			@Override
			public <T extends ResourcePackContainer> void registerContainer(Map<String, T> map, ResourcePackContainer.Factory<T> factory) {
				int i = 0;
				for (VirtualResourcePack pack : packs.get(type)) {
					String id = pack.getId(i++);
					T container = ResourcePackContainer.of(id, type == ResourceType.CLIENT_RESOURCES, () -> pack, factory, ResourcePackContainer.InsertionPosition.BOTTOM);
					if (container != null) {
						map.put(id, container);
					}
				}
			}
		};
	}

	/**
	 * Adds a virtual resource pack to the manager.
	 *
	 * @param pack the pack
	 * @param types the resource types that the pack provides
	 */
	public void addPack(VirtualResourcePack pack, Collection<ResourceType> types) {
		if (types.isEmpty()) {
			throw new IllegalArgumentException("Trying to add virtual resource pack with no types");
		}

		for (ResourceType type : types) {
			packs.put(type, pack);
		}
	}

	public ImmutableMultimap<ResourceType, VirtualResourcePack> getPacks() {
		return ImmutableMultimap.copyOf(packs);
	}
}
