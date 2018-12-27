package io.github.cottonmc.cotton.registry;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CottonItems {

	public static Item register(String name, Item item) {
		if (!Registry.ITEM.contains(new Identifier("cotton", name))) {
			Registry.register(Registry.ITEM, "cotton:" + name, item);
			return item;
		}
		else {
			return Registry.ITEM.get(new Identifier("cotton", name));
		}
	}

}
