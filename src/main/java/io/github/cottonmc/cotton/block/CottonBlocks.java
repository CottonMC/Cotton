package io.github.cottonmc.cotton.block;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.cauldron.StoneCauldronBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CottonBlocks {

	public static Block STONE_CAULDRON;

	public static void init() {
		STONE_CAULDRON = register("stone_cauldron", new StoneCauldronBlock(), ItemGroup.DECORATIONS);
	}

	public static Block register(String name, Block block, ItemGroup group) {
		Identifier id = new Identifier(Cotton.MODID, name);

		Registry.register(Registry.BLOCK, id, block);
		BlockItem item = new BlockItem(block, new Item.Settings().itemGroup(group));
		Registry.register(Registry.ITEM, id, item);

		return block;
	}
}
