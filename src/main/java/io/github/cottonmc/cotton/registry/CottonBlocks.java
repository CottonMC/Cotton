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

	public static Block register(String name, Block block, ItemGroup tab) {
		Identifier id = new Identifier("cotton", name);
		if (!Registry.BLOCK.contains(id)) {
			Registry.register(Registry.BLOCK, id, block);
			BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(tab));
			CottonItems.register(id, item);
			return block;
		}
		else {
			return registeredBlocks.get(id.getPath());
		}
	}

	public static Block register(String name, Block block) {
		Identifier id = new Identifier("cotton", name);
		if (!Registry.BLOCK.contains(id)) {
			Registry.register(Registry.BLOCK, id, block);
			BlockItem item = new BlockItem(block, new Item.Settings());
			CottonItems.register(id, item);
			return block;
		}
		else {
			return Registry.BLOCK.get(id);
		}
	}

}
