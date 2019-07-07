package io.github.cottonmc.cotton.commons;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name="CottonCommons")
public class CottonCommonsConfig {
    @Comment("Fill common tags automatically with modded items. This may add unintended items.\n"
            + "Intended for pack devs dealing with mods not using common tags.")
    public boolean fillCommonTags = false;
}