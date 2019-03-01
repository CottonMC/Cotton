package io.github.cottonmc.cotton.tags;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.impl.SyntaxError;
import io.github.cottonmc.cotton.Cotton;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TagEntryManager {
	public static void registerToTag(TagType type, Identifier tagId, String... entries) {
		Cotton.logger.info("Adding objects to tag " + tagId.toString());
		File tagFile = new File(Cotton.getTagLocation(tagId), type.asString() + "/" + tagId.getPath() + ".json");
		Jankson jankson = Jankson.builder().build();
		try {
			if (!tagFile.getParentFile().exists()) tagFile.getParentFile().mkdirs();
			TagFile tagContents;
			if (!tagFile.exists()) {
				tagFile.createNewFile();
				tagContents = new TagFile();
			} else {
				JsonObject currentJson = jankson.load(tagFile);
				tagContents = jankson.fromJson(currentJson, TagFile.class);
			}
			for (String entry : entries) {
				if (!tagContents.values.contains(entry)) tagContents.values.add(entry);
			}
			String result = jankson
					.toJson(tagContents)
					.toJson(false, true, 0);
			FileOutputStream out = new FileOutputStream(tagFile, false);
			out.write(result.getBytes());
			out.flush();
			out.close();
		}
		catch (IOException | SyntaxError e) {
			Cotton.logger.warn("Failed to generate tag " + tagId.toString() + ": " + e);
		}
	}
}
