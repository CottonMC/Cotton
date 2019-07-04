package io.github.cottonmc.cotton.datapack.loot.objects;

import blue.endless.jankson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class LootTableEntry {
	public String type;
	public String name;
	public int weight;
	public List<JsonObject> conditions = new ArrayList<>();
	public List<JsonObject> functions = new ArrayList<>();

	public LootTableEntry(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public LootTableEntry(String type, String name, int weight) {
		this(type, name);
		this.weight = weight;
	}

}
