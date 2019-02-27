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
			TagFile tagContents;
			try {
				JsonObject currentJson = jankson.load(tagFile);
				tagContents = jankson.fromJson(currentJson, TagFile.class);
			} catch (SyntaxError e) {
				Cotton.logger.warn("Tag file for " + tagId.toString() + " seems to be missing or malformed. Generating new file.");
				Cotton.logger.warn("Error: " + e);
				//TODO: Write to a `<tagId>.malformed.json` file?
				tagContents = new TagFile();
			}
			tagContents.values.addAll(Arrays.asList(entries));
			String result = jankson
					.toJson(tagContents)
					.toJson(false, true, 0);
			FileOutputStream out = new FileOutputStream(tagFile, false);
			out.write(result.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			Cotton.logger.warn("Failed to generate tag " + tagId.toString() + ": " + e);
		}
	}
}
