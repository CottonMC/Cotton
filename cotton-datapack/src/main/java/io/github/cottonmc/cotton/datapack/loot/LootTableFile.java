package io.github.cottonmc.cotton.datapack.loot;

import io.github.cottonmc.cotton.datapack.loot.objects.LootTablePool;

import java.util.ArrayList;
import java.util.List;

public class LootTableFile {
	public String type;
	public List<LootTablePool> pools = new ArrayList<>();

	public LootTableFile(String type) {
		this.type = type;
	}
}
