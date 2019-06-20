package io.github.cottonmc.cotton.datapack.virtual;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.resource.ResourcePackContainer;
import net.minecraft.resource.ResourcePackCreator;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.util.*;

/**
 * Manages virtual resource packs. See {@link #addPack(VirtualResourcePack, Collection)} for registering packs.
 */
public enum VirtualResourcePackManager {
	INSTANCE;

	private final Multimap<ResourceType, PackContainer> packs = MultimapBuilder.hashKeys().arrayListValues().build();
	private final Set<Identifier> packIds = new HashSet<>();

	public ResourcePackCreator getCreatorForType(ResourceType type) {
		return new ResourcePackCreator() {
			@Override
			public <T extends ResourcePackContainer> void registerContainer(Map<String, T> map, ResourcePackContainer.Factory<T> factory) {
				for (PackContainer packContainer : packs.get(type)) {
					VirtualResourcePack pack = packContainer.getPack();
					ClientResourcePackMode clientPackMode = packContainer.getClientPackMode();
					String id = "virtual/" + pack.getId();
					T container = ResourcePackContainer.of(
							id,
							type == ResourceType.CLIENT_RESOURCES && clientPackMode == ClientResourcePackMode.ALWAYS_ENABLED,
							() -> pack,
							factory,
							ResourcePackContainer.InsertionPosition.TOP
					);
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
	 * @param pack           the pack
	 * @param types          the resource types that the pack provides
	 * @param clientPackMode the client resource pack mode
	 */
	public void addPack(VirtualResourcePack pack, Collection<ResourceType> types, ClientResourcePackMode clientPackMode) {
		if (types.isEmpty()) {
			throw new IllegalArgumentException("Trying to add virtual resource pack with no types");
		} else if (packIds.contains(pack.getId())) {
			throw new IllegalArgumentException(String.format("Duplicate virtual resource pack ID: %s", pack.getId()));
		}

		for (ResourceType type : types) {
			packs.put(type, new PackContainer(pack, clientPackMode));
		}

		packIds.add(pack.getId());
	}

	/**
	 * Adds a virtual resource pack to the manager.
	 * If the pack is a client-side resource pack,
	 * it'll be {@linkplain ClientResourcePackMode#ALWAYS_ENABLED always enabled}.
	 *
	 * @param pack  the pack
	 * @param types the resource types that the pack provides
	 */
	public void addPack(VirtualResourcePack pack, Collection<ResourceType> types) {
		addPack(pack, types, ClientResourcePackMode.ALWAYS_ENABLED);
	}

	/**
	 * Gets all virtual packs as a resource type->pack multimap.
	 *
	 * @return the pack multimap
	 */
	public ImmutableMultimap<ResourceType, PackContainer> getPacks() {
		return ImmutableMultimap.copyOf(packs);
	}

	/**
	 * @return an immutable set of all virtual pack ids
	 */
	public ImmutableSet<Identifier> getPackIds() {
		return ImmutableSet.copyOf(packIds);
	}

	/**
	 * Behavior of resource packs in the client resource pack screen.
	 */
	public enum ClientResourcePackMode {
		/**
		 * The resource pack can be disabled, and is disabled by default.
		 */
		OPTIONAL,

		/**
		 * The resource pack is always enabled, and cannot be disabled.
		 */
		ALWAYS_ENABLED
	}

	public static final class PackContainer {
		private final VirtualResourcePack pack;
		private final ClientResourcePackMode clientPackMode;

		private PackContainer(VirtualResourcePack pack, ClientResourcePackMode clientPackMode) {
			this.pack = pack;
			this.clientPackMode = clientPackMode;
		}

		public VirtualResourcePack getPack() {
			return pack;
		}

		public ClientResourcePackMode getClientPackMode() {
			return clientPackMode;
		}
	}
}
