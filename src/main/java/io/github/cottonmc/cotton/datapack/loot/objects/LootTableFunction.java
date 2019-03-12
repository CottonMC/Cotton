package io.github.cottonmc.cotton.datapack.loot.objects;

import java.util.List;

public class LootTableFunction {
	public String function;
	public int levels;
	public boolean treasure;
	public List<LootTableCondition> conditions;
	public LootTableMeta count;
	public LootTableMeta damage;

	public LootTableFunction(String function) {
		this.function = function;
	}

	public class LootTableMeta {
		public int min;
		public int max;
		public String type;
	}
}
