package io.github.cottonmc.cotton;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.config.CottonConfig;
import io.github.cottonmc.cotton.logging.Ansi;
import io.github.cottonmc.cotton.logging.ModLogger;
import io.github.cottonmc.cotton.registry.CommonItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class Cotton implements ModInitializer {

	public static final String MODID = "cotton";

	public static final ItemGroup commonGroup = FabricItemGroupBuilder.build(new Identifier("cotton:common_tab"), new Supplier<ItemStack>() {
		@Override
		public ItemStack get() {
			return new ItemStack(Blocks.LIGHT_BLUE_WOOL);
		}
	});

	public static ModLogger logger = new ModLogger(MODID, "COTTON");
	public static CottonConfig config;

	@Override
	public void onInitialize() {
		logger.setPrefixFormat(Ansi.Bold.and(Ansi.Green));

		//example config and logger code
		config = ConfigManager.loadConfig(CottonConfig.class);
		logger.info("loaded config");
		logger.info("number 1 is "+config.number1);


	}
}
