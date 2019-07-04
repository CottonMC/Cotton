package io.github.cottonmc.cotton.datapack;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.datapack.config.CottonDatapackConfig;
import io.github.cottonmc.cotton.datapack.recipe.CottonRecipes;
import io.github.cottonmc.cotton.datapack.recipe.RecipeUtil;
import io.github.cottonmc.cotton.datapack.virtual.PackPrinterCommand;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import io.github.cottonmc.cotton.logging.ModLogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class CottonDatapack implements ModInitializer {

    public static final String MODID = "cotton-datapack";
    public static final String SHARED_NAMESPACE = "c";
    public static final ModLogger LOGGER = new ModLogger(MODID, "Cotton Datapack");
    public static final File DATA_PACK_LOCATION = new File(FabricLoader.getInstance().getGameDirectory(), "datapacks/cotton_generated");

    public static CottonDatapackConfig config;

    @Override
    public void onInitialize() {
		PackMetaManager.saveMeta();
		CottonRecipes.init();

        config = ConfigManager.loadConfig(CottonDatapackConfig.class);
        LOGGER.info("Loaded config.");

        RecipeUtil.init(config);

        //register the command that prints out the virtual data and resource packs.
        CommandRegistry.INSTANCE.register(false, new PackPrinterCommand());

        // EXAMPLE CODE START

//		HashMap<String, InputStreamProvider> map = new HashMap<>();
//        map.put("assets/minecraft/blockstates/cobblestone.json", InputStreamProvider.of(() -> "{\n" +
//                "    \"variants\": {\n" +
//                "        \"\": { \"model\": \"block/sandstone\" }\n" +
//                "    }\n" +
//                "}\n"));
//        VirtualResourcePackManager.INSTANCE.addPack(
//                new VirtualResourcePack(new Identifier("cotton", "test"), map),
//                Collections.singleton(ResourceType.CLIENT_RESOURCES)
//        );
//		TagEntryManager.registerToTag(TagType.BLOCK, new Identifier("minecraft:enderman_holdable"), "minecraft:string");
//		TagEntryManager.registerToTag(TagType.BLOCK, new Identifier("minecraft:dragon_immune"), "#minecraft:enderman_holdable");
//		LootTableManager.registerBasicBlockDropTable(new Identifier("minecraft", "dirt"));
//		RecipeUtil.removeRecipe(new Identifier("crafting_table"));

        // EXAMPLE CODE END
    }
}
