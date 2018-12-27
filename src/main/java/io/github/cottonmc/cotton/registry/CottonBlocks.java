package io.github.cottonmc.cotton.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class CottonBlocks {

	public static Map<String, Block> registeredBlocks;

	public static Block register(Identifier id, Block block, ItemGroup tab) {
		if (!registeredBlocks.containsKey(id.getPath())) {
			Registry.register(Registry.BLOCK, id, block);
			BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(tab));
			CottonItems.register(id, item);
			registeredBlocks.put(id.getPath(), block);
			return block;
		}
		else {
			return registeredBlocks.get(id.getPath());
		}
	}

	public static Block register(Identifier id, Block block) {
		if (!registeredBlocks.containsKey(id.getPath())) {
			Registry.register(Registry.BLOCK, id, block);
			BlockItem item = new BlockItem(block, new Item.Settings());
			CottonItems.register(id, item);
			registeredBlocks.put(id.getPath(), block);
			return block;
		}
		else {
			return registeredBlocks.get(id.getPath());
		}
	}
}
