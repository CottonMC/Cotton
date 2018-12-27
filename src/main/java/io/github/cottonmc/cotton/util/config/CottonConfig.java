package io.github.cottonmc.cotton.util.config;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.util.config.annotations.ConfigFile;

import java.util.ArrayList;
import java.util.List;

@ConfigFile(name="CottonConfig")
public class CottonConfig {

    @Comment(value="A list of mod ids, in order of preference for resource loading.")
    public List<String> namespacePreferenceOrder = new ArrayList<>();

}
