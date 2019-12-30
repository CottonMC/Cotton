package io.github.cottonmc.cotton.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;
import io.github.cottonmc.cotton.logging.ModLogger;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigManager {

    public static final ModLogger LOGGER = new ModLogger("cotton-config", "Cotton Config");
    public static final String DEFAULT_EXTENSION = ".json5";

    /**Loads a .config file from the config folder and parses it to a POJO.
     *
     * @param clazz The class of the POJO that will store all our properties
     * @return A new config Object containing all our options from the config file
     */
    public static <T> T loadConfig(Class<T> clazz) {
        String configName;
        if (clazz.isAnnotationPresent(ConfigFile.class)) {
            ConfigFile annotation = clazz.getAnnotation(ConfigFile.class);
            configName = annotation.name() + annotation.extension();
        } else {
            configName = clazz.getSimpleName() + DEFAULT_EXTENSION;
        }
        return loadConfig(clazz, configName);
    }

    /**Loads a .config file from the config folder and parses it to a POJO.
     *
     * @param clazz The class of the POJO that will store all our properties
     * @param configName The name of the config file
     * @return A new config Object containing all our options from the config file
     */
    public static <T> T loadConfig(Class<T> clazz, String configName){
        try {
            File file = FabricLoader.getInstance().getConfigDirectory().toPath().resolve(configName).toFile();
            Jankson jankson = Jankson.builder().build();

            //Generate config file if it doesn't exist
            if(!file.exists()) {
                saveConfig(clazz.newInstance(), configName);
            }

            try {
                JsonObject json = jankson.load(file);
                String cleaned = json.toJson(false,true); //remove comments

                T result = jankson.fromJson(json, clazz);

                //check if the config file is outdated. If so overwrite it
                JsonElement jsonElementNew = jankson.toJson(clazz.newInstance());
                if(jsonElementNew instanceof JsonObject){
                    JsonObject jsonNew = (JsonObject) jsonElementNew;
                    if(json.getDelta(jsonNew).size()>= 0){
                        saveConfig(result, configName);
                    }
                }

                return result;
            } catch (IOException e) {
                LOGGER.warn("Failed to load config File %s: %s", configName, e);
            }
        } catch (SyntaxError syntaxError) {
            LOGGER.warn("Failed to load config File %s: %s", configName, syntaxError);
        } catch (IllegalAccessException | InstantiationException e) {
            LOGGER.warn("Failed to create new config file for %s: %s", configName, e);
        }

        //Something obviously went wrong, create placeholder config
        LOGGER.warn("Creating placeholder config for %s...", configName);
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.warn("Failed to create placeholder config for %s: %s", configName, e);
        }

        //this is ... unfortunate
        return null;
    }

    /**Saves a POJO Config object to the disk and uses either the name specified in the Annotation (if available) or
     * the simple Class name as filename.
     * This function is used to create new configs if they don't already exist.
     * @param object The Config we want to save
     */
    public static void saveConfig(Object object){
        String configName;
        if (object.getClass().isAnnotationPresent(ConfigFile.class)) {
            ConfigFile annotation = object.getClass().getAnnotation(ConfigFile.class);
            configName = annotation.name() + annotation.extension();
        } else {
            configName = object.getClass().getSimpleName() + DEFAULT_EXTENSION;
        }
        saveConfig(object,configName);
    }

    /**Saves a POJO Config object to the disk. This is mostly used to create new configs if they don't already exist
     *
     * @param object The Config we want to save
     * @param configName The filename of our config.
     */
    public static void saveConfig(Object object, String configName){
        Jankson jankson = Jankson.builder().build();
        JsonElement json = jankson.toJson(object);
        String result = json.toJson(true,true);


        try {
            File file = FabricLoader.getInstance().getConfigDirectory().toPath().resolve(configName).toFile();
            if(!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(file,false);

            out.write(result.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
           LOGGER.warn("Failed to write to config file %s: %s", configName, e);
        }
    }

}
