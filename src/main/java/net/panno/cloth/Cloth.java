package net.panno.cloth;

import net.panno.cloth.block.ModBlocks;
import net.panno.cloth.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class InfraRedstone implements ModInitializer {

	public static final ItemGroup clothGroup = FabricItemGroupBuilder.build(new Identifier("cloth:cloth_tab"), () -> new ItemStack(Items.IRON_INGOT));

	@Override
	public void onInitialize() {
		ModBlocks.init();
		ModItems.init();

	}
}
