package io.github.cottonmc.cotton.config;

import blue.endless.jankson.Comment;
import blue.endless.jankson.JsonObject;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

import java.util.ArrayList;
import java.util.List;

@ConfigFile(name="CottonConfig")
public class CottonConfig {

	@Comment("A list of mod ids, in order of preference for resource loading.")
	public List<String> namespace_preference_order = new ArrayList<>();

	@Comment("Fill common tags automatically with modded items. This may add unintended items.\n"
			+ "Intended for pack devs dealing with mods not using common tags.")
	public boolean fillCommonTags = false;

	@Comment("Make it so that right-clicking a cauldron that has lava in it will void a non-bucket item in your hand.")
	public boolean cauldronTrashCan = false;

	@Comment("{'identifier': 'minecraft:coal_from_smelting'} will remove the specific vanilla smelting recipe for coal. {'item': 'minecraft:chest'} will remove *all* recipes that create vanilla chests.")
	public ArrayList<JsonObject> recipesToRemove = new ArrayList<>();

}
