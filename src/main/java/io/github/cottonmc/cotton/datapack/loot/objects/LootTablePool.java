package io.github.cottonmc.cotton.datapack.loot.objects;

import java.util.ArrayList;
import java.util.List;

public class LootTablePool {
	public int rolls;
	public List<LootTableEntry> entries = new ArrayList<>();
	public List<LootTableCondition> conditions = new ArrayList<>();

	public LootTablePool(int rolls) {
		this.rolls = rolls;
	}
}
