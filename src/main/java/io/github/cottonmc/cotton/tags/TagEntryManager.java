package io.github.cottonmc.cotton.tags;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.impl.SyntaxError;
import io.github.cottonmc.cotton.Cotton;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class TagEntryManager {
	public static void registerToTag(TagType type, Identifier tagId, String... entries) {
		Cotton.logger.info("Adding objects to tag " + tagId.toString());
		File tagFile = new File(Cotton.getTagLocation(tagId), type.asString() + "/" + tagId.getPath() + ".json");
		Jankson jankson = Jankson.builder().build();
		try {
			if (!tagFile.getParentFile().exists()) tagFile.getParentFile().mkdirs();
			if (!tagFile.exists()) tagFile.createNewFile();
			JsonObject currentJson = jankson.load(tagFile);
			TagFile tagContents = jankson.fromJson(currentJson, TagFile.class);
			tagContents.values.addAll(Arrays.asList(entries));
			String result = jankson
					.toJson(tagContents)
					.toJson(false, true, 0);
			FileOutputStream out = new FileOutputStream(tagFile, false);
			out.write(result.getBytes());
			out.flush();
			out.close();
		} catch (IOException | SyntaxError e) {
			Cotton.logger.warn("Failed to generate tag " + tagId.toString() + ": " + e);
		}
	}
}
