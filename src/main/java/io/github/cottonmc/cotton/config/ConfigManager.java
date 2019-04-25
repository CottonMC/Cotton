package io.github.cottonmc.cotton.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.impl.SyntaxError;
import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;
import io.github.cottonmc.cotton.datapack.recipe.RecipeUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigManager {

	private static final String CONFIG_FILE_EXTENSION = ".json5";

	public static Jankson getBaseJankson() {
		Jankson baseJankson = Jankson.builder()
				.registerTypeAdapter(ItemStack.class, RecipeUtil::getItemStack)
				.registerSerializer(ItemStack.class, (t, marshaller)->{ return RecipeUtil.saveItemStack(t); })
				.build();
		return baseJankson;
	}
	
	
	/**Loads a .config file from the config folder and parses it to a POJO.
	 *
	 * @param clazz The class of the POJO that will store all our properties
	 * @return A new config Object containing all our options from the config file
	 */
	public static <T> T loadConfig(Class<T> clazz) {
		String configName;
		if(clazz.isAnnotationPresent(ConfigFile.class)){
			configName = clazz.getAnnotation(ConfigFile.class).name();
		} else {
			configName = clazz.getSimpleName();
		}
		return loadConfig(clazz, configName);
	}
	
	public static <T> T loadConfig(Class<T> clazz, String configName) {
		return loadConfig(clazz, configName, getBaseJankson());
	}
	
	/**Loads a .config file from the config folder and parses it to a POJO.
	 *
	 * @param clazz The class of the POJO that will store all our properties
	 * @param configName The name of the config file
	 * @return A new config Object containing all our options from the config file
	 */
	public static <T> T loadConfig(Class<T> clazz, String configName, Jankson jankson){
		try {
			File file = new File((FabricLoader.getInstance()).getConfigDirectory().toString()+"/"+configName+CONFIG_FILE_EXTENSION);

			//Generate config file if it doesn't exist
			if(!file.exists()) {
				saveConfig(clazz.newInstance(), configName);
			}

			try {
				JsonObject json = jankson.load(file);
				//String cleaned = json.toJson(false,true); //remove comments
				System.out.println("json:"+json.toString());
				
				T result = jankson.getMarshaller().marshall(clazz, json);
				
				//T result = jankson.fromJson(json, clazz);
				System.out.println(result);

				//check if the config file is outdate. If so overwrite it
				JsonElement jsonElementNew = jankson.toJson(clazz.newInstance());
				if(jsonElementNew instanceof JsonObject){
					JsonObject jsonNew = (JsonObject) jsonElementNew;
					jsonNew.setMarshaller(jankson.getMarshaller());
					if(json.getDelta(jsonNew).size()>= 0){
						saveConfig(result, configName, jankson);
					}
				}

				return result;
			}
			catch (IOException e) {
				Cotton.logger.warn("Failed to load config File "+configName+CONFIG_FILE_EXTENSION+": ", e);
			}
		}
		catch (SyntaxError syntaxError) {
			Cotton.logger.warn("Failed to load config File "+configName+CONFIG_FILE_EXTENSION+": ", syntaxError);
		} catch (IllegalAccessException | InstantiationException e) {
			Cotton.logger.warn("Failed to create new config file for "+configName+CONFIG_FILE_EXTENSION+": ", e);
		}

		//Something obviously went wrong, create placeholder config
		Cotton.logger.warn("Creating placeholder config for "+configName+CONFIG_FILE_EXTENSION+"...");
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			Cotton.logger.warn("Failed to create placeholder config for "+configName+CONFIG_FILE_EXTENSION+": ",e);
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
		if(object.getClass().isAnnotationPresent(ConfigFile.class)){
			configName = object.getClass().getAnnotation(ConfigFile.class).name();
		} else {
			configName = object.getClass().getSimpleName();
		}
		saveConfig(object,configName);
	}

	public static void saveConfig(Object object, String configName) {
		saveConfig(object, configName, getBaseJankson());
	}
	
	/**Saves a POJO Config object to the disk. This is mostly used to create new configs if they don't already exist
	 *
	 * @param object The Config we want to save
	 * @param configName The filename of our config.
	 */
	public static void saveConfig(Object object, String configName, Jankson jankson){
		JsonElement json = jankson.toJson(object);
		String result = json.toJson(true,true);


		try {
			File file = new File((FabricLoader.getInstance()).getConfigDirectory().toString()+"/"+configName+CONFIG_FILE_EXTENSION);
			if(!file.exists())
				file.createNewFile();

			FileOutputStream out = new FileOutputStream(file,false);

			out.write(result.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			Cotton.logger.warn("Failed to write to config file"+configName+CONFIG_FILE_EXTENSION+": " + e);
		}
	}

}
