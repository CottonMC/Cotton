package io.github.cottonmc.cotton.datapack;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.*;

import java.io.File;
import java.io.FileFilter;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GlobalResourcePackProvider implements ResourcePackProvider {
	private static final FileFilter POSSIBLE_PACK = (file) -> {
		boolean isZip = file.isFile() && file.getName().endsWith(".zip");
		boolean hasMeta = file.isDirectory() && (new File(file, "pack.mcmeta")).isFile();
		return isZip || hasMeta;
	};
	private final File packsFolder;

	public GlobalResourcePackProvider() {
		this.packsFolder = new File(FabricLoader.getInstance().getGameDirectory(), "datapacks");
	}

	public <T extends ResourcePackProfile> void register(Consumer<T> packConsumer, ResourcePackProfile.Factory<T> factory) {
		if (!this.packsFolder.isDirectory()) {
			this.packsFolder.mkdirs();
		}

		File[] files = this.packsFolder.listFiles(POSSIBLE_PACK);
		if (files != null) {

			for(File file : files) {
				String name = "global/" + file.getName();
				T container = ResourcePackProfile.of(name, false, this.createResourcePack(file), factory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.method_29486("global"));
				if (container != null) {
					packConsumer.accept(container);
				}
			}

		}
	}

	private Supplier<ResourcePack> createResourcePack(File file) {
		return file.isDirectory() ? () -> new DirectoryResourcePack(file) : () -> new ZipResourcePack(file);
	}
}
