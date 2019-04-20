package io.github.cottonmc.cotton.datapack.recipe;

import io.github.cottonmc.cotton.Cotton;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecipeUtil {

	public static void removeRecipe(Identifier id) {
		File recipeFile = new File(getRecipeLocation(id), id.getPath() + ".json");
		try {
			if (!recipeFile.getParentFile().exists()) recipeFile.getParentFile().mkdirs();
			if (!recipeFile.exists()) recipeFile.createNewFile();
			String result = "{ \"type\": \"cotton:null\" }";
			FileOutputStream out = new FileOutputStream(recipeFile, false);
			out.write(result.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			Cotton.logger.warn("Failed to remove recipe " + id.toString() + ": " + e);
		}
	}

	public static File getRecipeLocation(Identifier id) {
		return new File(Cotton.DATA_PACK_LOCATION, "data/" + id.getNamespace() + "/recipes");

	}
}
