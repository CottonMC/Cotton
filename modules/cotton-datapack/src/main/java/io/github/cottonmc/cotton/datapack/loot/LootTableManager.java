package io.github.cottonmc.cotton.datapack.loot;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import io.github.cottonmc.cotton.datapack.CottonDatapack;
import io.github.cottonmc.cotton.datapack.loot.objects.LootTableEntry;
import io.github.cottonmc.cotton.datapack.loot.objects.LootTablePool;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class LootTableManager {
	/**
	 * Creates a basic block drop loot table.
	 * NOTE: Unless you're dynamically creating/registering blocks, only use this in a development environment to generate tables.
	 * @param id the namespaced ID of the block to register for
	 */
	public static void registerBasicBlockDropTable(Identifier id) {
		LootTableEntry selfEntry = new LootTableEntry("minecraft:item", id.toString());
		JsonObject standardCondition = new JsonObject();
		standardCondition.put("condition", new JsonPrimitive("minecraft:survives_explosion"));
		LootTablePool mainPool = new LootTablePool(1);
		mainPool.entries.add(selfEntry);
		mainPool.conditions.add(standardCondition);
		buildLootTable(id, "blocks","minecraft:block", mainPool);
	}

	/**
	 * Creates and saves a loot table JSON.
	 * NOTE: Unless you're dynamically creating loot tables, only use this in a development environment to ggenerate tables.
	 * @param id the namespaced ID of the loot table file
	 * @param subfolder the subfolder in "loot_tables" the table goes in: blocks, chests, entities, or gameplay
	 * @param type the namespaced type of loot table; see vanilla loot tables for examples
	 * @param pools the pools for the loot table to draw from
	 */
	public static void buildLootTable(Identifier id, String subfolder, String type, LootTablePool...pools) {
		CottonDatapack.LOGGER.info("Adding loot table for " + id.toString());
		File tableFile = new File(getTableLocation(id), subfolder + "/" + id.getPath() + ".json");
		Jankson jankson = Jankson.builder().build();
		try {
			if (!tableFile.getParentFile().exists()) tableFile.getParentFile().mkdirs();
			if (!tableFile.exists()) tableFile.createNewFile();
			LootTableFile table = new LootTableFile(type);
			table.pools.addAll(Arrays.asList(pools));
			String result = jankson
					.toJson(table)
					.toJson(false, true, 0);
			FileOutputStream out = new FileOutputStream(tableFile, false);
			out.write(result.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			CottonDatapack.LOGGER.warn("Failed to generate loot table " + id.toString() + ": " + e);
		}
	}

	public static File getTableLocation(Identifier id) {
		return new File(CottonDatapack.DATA_PACK_LOCATION, "data/" + id.getNamespace() + "/loot_tables");

	}
}
