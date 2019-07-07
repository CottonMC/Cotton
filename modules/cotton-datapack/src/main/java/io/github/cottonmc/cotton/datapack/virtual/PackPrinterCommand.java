package io.github.cottonmc.cotton.datapack.virtual;

import com.mojang.brigadier.CommandDispatcher;
import io.github.cottonmc.cotton.datapack.CottonDatapack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Outputs the requested resource/data packs into a specific folder, so they can be read by pack makers.
 */
public final class PackPrinterCommand implements Consumer<CommandDispatcher<ServerCommandSource>> {
	private static final Logger LOGGER = LogManager.getLogger();

	@Override
	public void accept(CommandDispatcher<ServerCommandSource> dispatcher) {
		//create a command with one argument: which type of resource we want to export.
		dispatcher.register(
				CommandManager.literal("datapack").then(CommandManager.literal("exportvirtual")
						.then(CommandManager.literal("assets")
								.executes(c -> {
									export(c.getSource(), ResourceType.CLIENT_RESOURCES);
									return 1;
								})
						)
						.then(CommandManager.literal("data")
								.executes(c -> {
									export(c.getSource(), ResourceType.SERVER_DATA);
									return 1;
								}))
				)
		);
	}

	/**
	 * Exports all of the required virtual packs.
	 */
	private static void export(ServerCommandSource source, ResourceType type) {
		try {
			//if we're not on a dedicated server. This will be removed when moving to client sidecommands.
			if (!source.getMinecraftServer().isDedicated()) {
				Map<ResourceType, Collection<VirtualResourcePackManager.PackContainer>> typeCollectionMap = VirtualResourcePackManager.INSTANCE.getPacks().asMap();
				Collection<VirtualResourcePackManager.PackContainer> virtualResourcePacks = typeCollectionMap.getOrDefault(type, Collections.emptyList());

				Path gameDir = FabricLoader.getInstance().getGameDirectory().toPath();
				Path exportedVirtualPacks = gameDir.resolve("exportedVirtualPacks");

				//create the folder
				Files.createDirectories(exportedVirtualPacks);
				if (Files.notExists(exportedVirtualPacks)) {
					source.sendError(new TranslatableText("message." + CottonDatapack.MODID + ".failed_to_create_folder", exportedVirtualPacks.toString()));
				} else {
					//loop through all of the virtual resource packs of the required type.
					for (VirtualResourcePackManager.PackContainer packContainer : virtualResourcePacks) {
						Map<String, InputStreamProvider> contents = packContainer.getPack().getContents();

						//write out all of our entries.
						for (Map.Entry<String, InputStreamProvider> entry : contents.entrySet()) {
							String location = entry.getKey();
							InputStreamProvider inputProvider = entry.getValue();
							Path outputPath = exportedVirtualPacks.resolve(location);
							Files.createDirectories(outputPath.getParent());

							try (InputStream in = inputProvider.create();
								 OutputStream out = Files.newOutputStream(outputPath)) {
								IOUtils.copy(in, out);
								out.flush();
							} catch (IOException e) {
								LOGGER.error("Failed to export virtual resource " + location, e);
								source.sendError(new TranslatableText("message." + CottonDatapack.MODID + ".exportvirtual.failed_to_export_resource", outputPath.toString()));
								return;
							}
						}
					}

					source.sendFeedback(new TranslatableText("message." + CottonDatapack.MODID + ".exportvirtual.exported"), true);
				}
			} else {
				source.sendError(new TranslatableText("message." + CottonDatapack.MODID + ".exportvirtual.dedicated_error"));
			}
		} catch (IOException e) {
			LOGGER.error("Failed to export virtual resources", e);
			source.sendError(new TranslatableText("message." + CottonDatapack.MODID + ".exportvirtual.failed_to_export_resources"));
		}
	}
}
