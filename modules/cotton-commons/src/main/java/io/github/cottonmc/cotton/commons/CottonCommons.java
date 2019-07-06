package io.github.cottonmc.cotton.commons;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.logging.ModLogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class CottonCommons implements ModInitializer {

	public static final String MODID = "cotton-commons";

	public static final ItemGroup commonGroup = FabricItemGroupBuilder.build(
			new Identifier("cotton-commons", "common_tab"), () -> new ItemStack(Blocks.LIGHT_BLUE_WOOL));

	public static final ModLogger logger = new ModLogger(MODID, "COTTON COMMONS");
	public static final CottonCommonsConfig config = ConfigManager.loadConfig(CottonCommonsConfig.class);;

	@Override
	public void onInitialize() {
		CommonTags.init();
	}
}
