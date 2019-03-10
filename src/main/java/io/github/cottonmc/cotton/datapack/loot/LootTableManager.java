package io.github.cottonmc.cotton.datapack.loot;

import blue.endless.jankson.Jankson;
import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.datapack.loot.objects.LootTableCondition;
import io.github.cottonmc.cotton.datapack.loot.objects.LootTableEntry;
import io.github.cottonmc.cotton.datapack.loot.objects.LootTablePool;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class LootTableManager {
	public static void registerBasicBlockDropTable(Identifier id) {
		Cotton.logger.info("Adding block drops for " + id.toString());
		File tableFile = new File(getTableLocation(id), "blocks/" + id.getPath() + ".json");
		Jankson jankson = Jankson.builder().build();
		try {
			if (!tableFile.getParentFile().exists()) tableFile.getParentFile().mkdirs();
			if (!tableFile.exists()) tableFile.createNewFile();
			LootTableFile table = new LootTableFile("minecraft:block");
			LootTableEntry selfEntry = new LootTableEntry("minecraft:item", id.toString());
			LootTableCondition standardCondition = new LootTableCondition("minecraft:survives_explosion");
			LootTablePool mainPool = new LootTablePool(1);
			mainPool.entries.add(selfEntry);
			mainPool.conditions.add(standardCondition);
			table.pools.add(mainPool);
			String result = jankson
					.toJson(table)
					.toJson(false, true, 0);
			FileOutputStream out = new FileOutputStream(tableFile, false);
			out.write(result.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			Cotton.logger.warn("Failed to generate loot table " + id.toString() + ": " + e);
		}
	}

	public static File getTableLocation(Identifier id) {
		return new File(Cotton.DATA_PACK_LOCATION, "data/" + id.getNamespace() + "/loot_tables");

	}
}
