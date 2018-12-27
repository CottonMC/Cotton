package io.github.cottonmc;

import io.github.cottonmc.block.ModBlocks;
import io.github.cottonmc.util.config.ConfigManager;
import io.github.cottonmc.util.config.CottonConfig;
import io.github.cottonmc.util.logging.Ansi;
import io.github.cottonmc.util.logging.ModLogger;
import net.minecraft.item.ItemStack;
import io.github.cottonmc.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Cotton implements ModInitializer {

	public static final String MODID = "cotton";

	// TODO: remove this. We only add new Blocks in cotton-resources, so we don't need a new tab for this mod.
	public static final ItemGroup cottonGroup = FabricItemGroupBuilder.build(new Identifier("cotton:cotton_tab"), () -> new ItemStack(Items.IRON_INGOT));


	public static ModLogger logger = new ModLogger(MODID, "COTTON");
	public static CottonConfig config;

	@Override
	public void onInitialize() {
		logger.setPrefixFormat(Ansi.Bold.and(Ansi.Green));

		//example config and logger code
		config = ConfigManager.loadConfig(CottonConfig.class);
		logger.info("loaded config");
		logger.info("number 1 is "+config.number1);

		// TODO: remove this. We only add new Blocks in cotton-resources
		ModBlocks.init();
		ModItems.init();

	}
}
