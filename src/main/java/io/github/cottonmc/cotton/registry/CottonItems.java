package io.github.cottonmc.cotton.registry;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CottonItems {

	public static Item register(String name, Item item) {
		Identifier id = new Identifier("cotton", name);
		if (!Registry.ITEM.contains(id)) {
			Registry.register(Registry.ITEM, id, item);
			return item;
		}
		else {
			return Registry.ITEM.get(id);
		}
	}

}
