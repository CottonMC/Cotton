package io.github.cottonmc.cotton.registry;

import io.github.cottonmc.cotton.Cotton;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class CottonItems {

	public static Map<String, Item> registeredItems;
	public static Map<String, Identifier> registeredItemNames;

	public static Item register(Identifier id, Item item) {
		if (canInsert(id)) {
			Registry.register(Registry.ITEM, id, item);
			registeredItems.put(id.getPath(), item);
			registeredItemNames.put(id.getPath(), id);
			return item;
		}
		else {
			return registeredItems.get(id.getPath());
		}
	}

	public static boolean canInsert(Identifier id) {
		if (!registeredItems.containsKey(id.getPath())) return true;
		if (Cotton.config == null || Cotton.config.namespacePreferenceOrder.size() == 0) return false;
		for (int i = 0; i < Cotton.config.namespacePreferenceOrder.size(); i++) {
			if (registeredItemNames.get(id.getPath()).getNamespace().equals(Cotton.config.namespacePreferenceOrder.get(i)) && registeredItemNames.get(id.getPath()).equals(id)) return false;
			if (id.getNamespace().equals(Cotton.config.namespacePreferenceOrder.get(i))) return true;
		}
		return false;
	}

}
