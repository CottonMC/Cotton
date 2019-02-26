package io.github.cottonmc.cotton;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.config.CottonConfig;
import io.github.cottonmc.cotton.logging.Ansi;
import io.github.cottonmc.cotton.logging.ModLogger;
import io.github.cottonmc.cotton.tags.PackMetaManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.io.File;

public class Cotton implements ModInitializer {

	public static final String MODID = "cotton";
	public static final boolean isDevEnv = Boolean.parseBoolean(System.getProperty("fabric.development", "false"));

	public static final File DATA_PACK_LOCATION = new File(FabricLoader.getInstance().getGameDirectory(), "datapacks/cotton (generated)/");
	public static File getTagLocation(Identifier tagId) {
		return new File(DATA_PACK_LOCATION, "data/" + tagId.getNamespace() + "/tags");
	}

	public static final ItemGroup commonGroup = FabricItemGroupBuilder.build(
			new Identifier("cotton", "common_tab"), () -> new ItemStack(Blocks.LIGHT_BLUE_WOOL));

	public static final ModLogger logger = new ModLogger(MODID, "COTTON");
	public static CottonConfig config;

	@Override
	public void onInitialize() {
		logger.setPrefixFormat(Ansi.Blue);

		//example config and logger code
		config = ConfigManager.loadConfig(CottonConfig.class);
		logger.info("Loaded config.");
		PackMetaManager.saveMeta();
		//TagEntryManager.registerToTag(TagType.BLOCK, new Identifier("minecraft:enderman_holdable"), "minecraft:string");
		//TagEntryManager.registerToTag(TagType.BLOCK, new Identifier("minecraft:dragon_immune"), "#minecraft:enderman_holdable");
	}
}
