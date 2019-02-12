package io.github.cottonmc.cotton;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.config.CottonConfig;
import io.github.cottonmc.cotton.logging.Ansi;
import io.github.cottonmc.cotton.logging.ModLogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Cotton implements ModInitializer {

	public static final String MODID = "cotton";
	public static boolean isDevEnv = Boolean.parseBoolean(System.getProperty("fabric.development", "false"));


	public static final ItemGroup commonGroup = FabricItemGroupBuilder.build(
			new Identifier("cotton", "common_tab"), () -> new ItemStack(Blocks.LIGHT_BLUE_WOOL));

	public static ModLogger logger = new ModLogger(MODID, "COTTON");
	public static CottonConfig config;

	@Override
	public void onInitialize() {
		logger.setPrefixFormat(Ansi.Green);

		//example config and logger code
		config = ConfigManager.loadConfig(CottonConfig.class);
		logger.info("loaded config");
		logger.error("OH SHIT; THINGS ARE WORKING!");
	}
}
