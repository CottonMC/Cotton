package io.github.cottonmc;

import io.github.cottonmc.block.ModBlocks;
import net.minecraft.item.ItemStack;
import io.github.cottonmc.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Cotton implements ModInitializer {

	public static final ItemGroup cottonGroup = FabricItemGroupBuilder.build(new Identifier("cotton:cotton_tab"), () -> new ItemStack(Items.IRON_INGOT));

	@Override
	public void onInitialize() {
		ModBlocks.init();
		ModItems.init();

	}
}
