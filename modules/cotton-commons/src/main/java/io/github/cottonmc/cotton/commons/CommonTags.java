package io.github.cottonmc.cotton.commons;

import io.github.cottonmc.cotton.datapack.CottonDatapack;
import io.github.cottonmc.cotton.datapack.tags.TagEntryManager;
import io.github.cottonmc.cotton.datapack.tags.TagType;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class CommonTags {
	//auto-fillable tags for common decorative/functional blocks
	public static final Tag.Identified<Item> GLASS_BLOCKS = registerItemTag("glass_blocks");
	public static final Tag.Identified<Item> GLASS_PANES = registerItemTag("glass_panes");
	public static final Tag.Identified<Item> TERRACOTTA = registerItemTag("terracotta");
	public static final Tag.Identified<Item> PRESSURE_PLATES = registerItemTag("pressure_plates");

	//tags for common item categories
	public static final Tag.Identified<Item> MUSHROOMS = registerItemTag("mushrooms");
	public static final Tag.Identified<Item> RAW_MEAT = registerItemTag("raw_meat");
	public static final Tag.Identified<Item> COOKED_MEAT = registerItemTag("cooked_meat");
	public static final Tag.Identified<Item> PLANTABLES = registerItemTag("plantables");

	//taggs for cotton functions
	public static final Tag.Identified<Block> CAULDRON_FIRE = (Tag.Identified<Block>)TagRegistry.block(new Identifier(CottonCommons.MODID, "cauldron_fire"));

	public static void init() {
		if (CottonCommons.CONFIG.fillCommonTags) {
			CottonCommons.LOGGER.info("Generating common tags!");
			List<String> glass_blocks = new ArrayList<>();
			List<String> glass_panes = new ArrayList<>();
			List<String> terracotta = new ArrayList<>();
			List<String> pressure_plates = new ArrayList<>();

			for (Identifier id : Registry.ITEM.getIds()) {
				//vanilla entries are pre-loaded
				if (id.getNamespace().equals("minecraft")) continue;
				if (id.getPath().contains("glass") && Registry.ITEM.get(id) instanceof BlockItem) {
					if (id.getPath().contains("pane")) glass_panes.add(id.toString());
					else glass_blocks.add(id.toString());
				}
				if (id.getPath().contains("terracotta")) terracotta.add(id.toString());
				if (id.getPath().contains("pressure_plate")) pressure_plates.add(id.toString());
			}

			if (!glass_blocks.isEmpty()) TagEntryManager.registerToTag(TagType.ITEM, GLASS_BLOCKS.getId(), glass_blocks.toArray(new String[0]));
			if (!glass_panes.isEmpty()) TagEntryManager.registerToTag(TagType.ITEM, GLASS_PANES.getId(), glass_panes.toArray(new String[0]));
			if (!terracotta.isEmpty()) TagEntryManager.registerToTag(TagType.ITEM, TERRACOTTA.getId(), terracotta.toArray(new String[0]));
			if (!pressure_plates.isEmpty()) TagEntryManager.registerToTag(TagType.ITEM, PRESSURE_PLATES.getId(), pressure_plates.toArray(new String[0]));
		}
	}

	private static Tag.Identified<Item> registerItemTag(String id) {
		return (Tag.Identified<Item>)TagRegistry.item(new Identifier(CottonDatapack.SHARED_NAMESPACE, id));
	}

	private static Tag.Identified<Block> registerBlockTag(String id) {
		return (Tag.Identified<Block>)TagRegistry.block(new Identifier(CottonDatapack.SHARED_NAMESPACE, id));
	}
}
