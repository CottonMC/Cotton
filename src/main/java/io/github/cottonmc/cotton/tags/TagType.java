package io.github.cottonmc.cotton.tags;

import net.minecraft.util.StringRepresentable;

public enum TagType implements StringRepresentable {
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
