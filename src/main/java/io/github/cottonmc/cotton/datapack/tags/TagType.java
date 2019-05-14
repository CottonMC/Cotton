package io.github.cottonmc.cotton.datapack.tags;

import net.minecraft.util.StringIdentifiable;

public enum TagType implements StringIdentifiable {
	BLOCK("blocks"), ITEM("items"), FLUID("fluids"), ENTITY_TYPE("entity_types");

	private final String name;

	TagType(String name) {
		this.name=name;
	}

	@Override
	public String asString() {
		return name;
	}
}
