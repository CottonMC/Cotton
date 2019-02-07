package io.github.cottonmc.cotton.config;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

import java.util.ArrayList;
import java.util.List;

@ConfigFile(name="CottonConfig")
public class CottonConfig {

    @Comment("A list of mod ids, in order of preference for resource loading.")
    public static List<String> namespace_preference_order = new ArrayList<>();

    @Comment("Add some sample splashes. Very dank ones, indeed.")
    public static boolean add_cotton_splashes = true;

    @Comment("Prioritize user-added splashes instead of vanilla ones.")
    public static boolean prioritize_custom_splashes = true;

    @Comment("If custom splashes are prioritized, how many times so? " +
            "1 is equal priority, 2 is '2 to 1', 3 is '3 to 1', etc.")
    public static int custom_splash_priority = 3;

    @Comment("Enable the Cotton Tweaks module.")
    public boolean include_tweaks = true;

    @Comment("Enable dispenser tweaks, like seed planting?")
    public static boolean enable_dispenser_tweaks = true;

}
