package io.github.cottonmc.cotton.base;

import io.github.cottonmc.cotton.base.config.CottonBaseConfig;
import io.github.cottonmc.cotton.base.registry.CommonTags;
import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.datapack.PackMetaManager;
import io.github.cottonmc.cotton.datapack.recipe.CottonRecipes;
import io.github.cottonmc.cotton.logging.ModLogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class CottonBase implements ModInitializer {

	public static final String MODID = "cotton-base";

	public static final ItemGroup commonGroup = FabricItemGroupBuilder.build(
			new Identifier("cotton-base", "common_tab"), () -> new ItemStack(Blocks.LIGHT_BLUE_WOOL));

	public static final ModLogger logger = new ModLogger(MODID, "Cotton Base");
	public static CottonBaseConfig config;

	@Override
	public void onInitialize() {
		PackMetaManager.saveMeta();
		CottonRecipes.init();

		config = ConfigManager.loadConfig(CottonBaseConfig.class);
		logger.info("Loaded config.");

		CommonTags.init();
	}
}
