package io.github.cottonmc.cotton.config;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

import java.util.ArrayList;
import java.util.List;

@ConfigFile(name="CottonConfig")
public class CottonConfig {

    @Comment("A list of mod ids, in order of preference for resource loading.")
    public static List<String> namespace_preference_order = new ArrayList<>();

    @Comment("Enable the Cotton Tweaks module.")
    public boolean include_tweaks = true;

    @Comment("Enable dispenser tweaks, like seed planting?")
    public static boolean enable_dispenser_tweaks = true;

}
