package io.github.cottonmc.cotton.registry;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class CottonItems {

	public static Map<String, Item> registeredItems;

	public static Item register(Identifier id, Item item) {
		if (!registeredItems.containsKey(id.getPath())) {
			Registry.register(Registry.ITEM, id, item);
			registeredItems.put(id.getPath(), item);
			return item;
		}
		else {
			return registeredItems.get(id.getPath());
		}
	}

}
