package net.panno.cloth.item;

import net.panno.cloth.InfraRedstone;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModItems {


	public static void init() {

	}

	public static Item register(String name, Item item) {
		Registry.register(Registry.ITEM, "cloth:" + name, item);
		return item;
	}

	public static Item register(ItemBase item) {
		Registry.register(Registry.ITEM, "cloth:" + item.getName(), item);
		return item;
	}
}
