package net.panno.cloth.block;

import net.panno.cloth.InfraRedstone;
import net.panno.cloth.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.block.BlockItem;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;



public class ModBlocks {

	public static void init() {

	}

	public static Block register(NamedBlock block, ItemGroup tab) {
		Registry.register(Registry.BLOCK, "cloth:" + block.getName(), block.getBlock());
		BlockItem item = new BlockItem(block.getBlock(), new Item.Settings().itemGroup(tab));
		ModItems.register(block.getName(), item);
		return block.getBlock();
	}

	public static BlockEntityType register(String name, Supplier<BlockEntity> be) {
		return Registry.register(Registry.BLOCK_ENTITY, "cloth:" + name, BlockEntityType.Builder.create(be).method_11034(null));
	}
}
