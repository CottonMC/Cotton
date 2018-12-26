package net.cottonmc.cloth;

import net.minecraft.item.ItemStack;
import net.cottonmc.cloth.block.ModBlocks;
import net.cottonmc.cloth.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Cloth implements ModInitializer {

	public static final ItemGroup clothGroup = FabricItemGroupBuilder.build(new Identifier("cloth:cloth_tab"), () -> new ItemStack(Items.IRON_INGOT));

	@Override
	public void onInitialize() {
		ModBlocks.init();
		ModItems.init();

	}
}
