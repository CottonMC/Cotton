package net.cottonmc.cloth.item;

import net.cottonmc.cloth.Cloth;
import net.minecraft.item.Item;

public class ItemBase extends Item {

	protected String name;

	public static Settings DEFAULT_SETTINGS = new Item.Settings().itemGroup(Cloth.clothGroup);

	public ItemBase(String name, Settings settings) {
		super(settings);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	//add any other helpful stuff here
}
