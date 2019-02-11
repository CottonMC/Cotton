package io.github.cottonmc.cotton.config;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

import java.util.ArrayList;
import java.util.List;

@ConfigFile(name="CottonConfig")
public class CottonConfig {

    @Comment("A list of mod ids, in order of preference for resource loading.")
    public List<String> namespace_preference_order = new ArrayList<>();

}
