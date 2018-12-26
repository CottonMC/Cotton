package net.cottonmc.cloth.item;

import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModItems {

	public static final Item COPPER_INGOT = register(new ItemBase("copper_ingot", ItemBase.DEFAULT_SETTINGS));
	public static final Item STEEL_INGOT = register(new ItemBase("steel_ingot", ItemBase.DEFAULT_SETTINGS));
	public static final Item SILVER_INGOT = register(new ItemBase("silver_ingot", ItemBase.DEFAULT_SETTINGS));
	public static final Item LEAD_INGOT = register(new ItemBase("lead_ingot", ItemBase.DEFAULT_SETTINGS));

	public static void init() {

	}

	public static Item register(String name, Item item) {
		Registry.register(Registry.ITEM, "cloth:" + name, item);
		return item;
	}

	public static Item register(ItemBase item) {
		register(item.getName(), item);
		return item;
	}
}
