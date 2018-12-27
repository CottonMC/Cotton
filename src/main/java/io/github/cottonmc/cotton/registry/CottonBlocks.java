package io.github.cottonmc.cotton.registry;

import io.github.cottonmc.cotton.Cotton;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class CottonBlocks {

	public static Map<String, Block> registeredBlocks;
	public static Map<String, Identifier> registeredBlockNames;

	public static Block register(Identifier id, Block block, ItemGroup tab) {
		if (canInsert(id)) {
			Registry.register(Registry.BLOCK, id, block);
			BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(tab));
			CottonItems.register(id, item);
			registeredBlocks.put(id.getPath(), block);
			registeredBlockNames.put(id.getPath(), id);
			return block;
		}
		else {
			return registeredBlocks.get(id.getPath());
		}
	}

	public static Block register(Identifier id, Block block) {
		if (canInsert(id)) {
			Registry.register(Registry.BLOCK, id, block);
			BlockItem item = new BlockItem(block, new Item.Settings());
			CottonItems.register(id, item);
			registeredBlocks.put(id.getPath(), block);
			registeredBlockNames.put(id.getPath(), id);
			return block;
		}
		else {
			return registeredBlocks.get(id.getPath());
		}
	}

	public static boolean canInsert(Identifier id) {
		if (!registeredBlocks.containsKey(id.getPath())) return true;
		if (Cotton.config == null || Cotton.config.namespacePreferenceOrder.size() == 0) return false;
		for (int i = 0; i < Cotton.config.namespacePreferenceOrder.size(); i++) {
			if (registeredBlockNames.get(id.getPath()).getNamespace().equals(Cotton.config.namespacePreferenceOrder.get(i)) && registeredBlockNames.get(id.getPath()).equals(id)) return false;
			if (id.getNamespace().equals(Cotton.config.namespacePreferenceOrder.get(i))) return true;
		}
		return false;
	}
}
