package io.github.cottonmc.cotton.registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CottonBlocks {

	public static Block register(String name, Block block, ItemGroup tab) {
		if (!Registry.BLOCK.contains(new Identifier("cotton", name))) {
			Registry.register(Registry.BLOCK, new Identifier("cotton", name), block);
			BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(tab));
			CottonItems.register(name, item);
			return block;
		}
		else {
			return Registry.BLOCK.get(new Identifier("cotton", name));
		}
	}
}
