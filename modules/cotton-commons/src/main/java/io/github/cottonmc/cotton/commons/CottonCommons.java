package io.github.cottonmc.cotton.commons;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.logging.ModLogger;
import net.fabricmc.api.ModInitializer;

public class CottonCommons implements ModInitializer {

	public static final String MODID = "cotton-commons";

	/**
	 * @deprecated The "Common Items" group, and all registration logic for it, is moving to the "Cotton Resources" mod.
	 */
//	@Deprecated
//	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
//			new Identifier("cotton-commons", "common_item_group"), () -> new ItemStack(Blocks.LIGHT_BLUE_WOOL));

	public static final ModLogger LOGGER = new ModLogger(MODID, "COTTON COMMONS");
	public static final CottonCommonsConfig CONFIG = ConfigManager.loadConfig(CottonCommonsConfig.class);

	@Override
	public void onInitialize() {
		CommonTags.init();
	}
}
