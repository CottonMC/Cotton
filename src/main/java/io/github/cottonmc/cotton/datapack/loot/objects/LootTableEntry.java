package io.github.cottonmc.cotton.datapack.loot.objects;

import java.util.ArrayList;
import java.util.List;

public class LootTableEntry {
	public String type;
	public String name;
	public int weight;
	public List<LootTableCondition> conditions = new ArrayList<>();
	public List<LootTableFunction> functions = new ArrayList<>();

	public LootTableEntry(String type, String name) {
		this.type = type;
		this.name = name;
	}

	public LootTableEntry(String type, String name, int weight) {
		this(type, name);
		this.weight = weight;
	}

}
