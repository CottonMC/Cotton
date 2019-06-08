package io.github.cottonmc.cotton.datapack.virtual;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.cottonmc.cotton.Cotton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Language;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Outputs the requested resource/data packs into a specific folder, so they can be read by pack makers.
 */
public class PackPrinterCommand implements Consumer<CommandDispatcher<ServerCommandSource>> {

    //an error type for the cases when the resource type was not typed in properly.
    private static final DynamicCommandExceptionType INVALID_INPUT = new DynamicCommandExceptionType((object_1) -> new TranslatableComponent("commands." + Cotton.MODID + ".exportvirtual.invalid_input", new Object[]{object_1}));

    @Override
    public void accept(CommandDispatcher<ServerCommandSource> dispatcher) {

        //create a command with one argument: which type of resource we want to export.
        dispatcher.register(
                LiteralArgumentBuilder.<ServerCommandSource>literal("exportvirtual")
                        .then(RequiredArgumentBuilder.<ServerCommandSource, ResourceType>argument("type", ResourceArgumentType.create())
                                .executes(c -> {
                                    ResourceType input = c.getArgument("type", ResourceType.class);
                                    if (ResourceType.CLIENT_RESOURCES == input) {
                                        export(c.getSource(), ResourceType.CLIENT_RESOURCES);
                                        return 1;
                                    }
                                    if (ResourceType.SERVER_DATA == input) {
                                        export(c.getSource(), ResourceType.SERVER_DATA);
                                        return 1;
                                    }

                                    return -1;
                                })
                        )
        );
    }

    /**
     * Exports all of the reuired virtual packs
     * */
    private static void export(ServerCommandSource serverCommandSource, ResourceType type) {
        //if we're not on a dedicated server. This will be removed when moving to client sidecommands.
        if (!serverCommandSource.getMinecraftServer().isDedicated()) {
            System.out.println("we're not dedicated, we can export");
            Map<ResourceType, Collection<VirtualResourcePack>> typeCollectionMap = VirtualResourcePackManager.INSTANCE.packs.asMap();

            Collection<VirtualResourcePack> virtualResourcePacks = typeCollectionMap.getOrDefault(type, Collections.emptyList());

            Path path = MinecraftClient.getInstance().runDirectory.getAbsoluteFile().toPath();
            Path exportedVirtualPacks = Paths.get(path.toString(), "exportedVirtualPacks");

            //create the folder
            exportedVirtualPacks.toFile().mkdirs();
            if (!exportedVirtualPacks.toFile().exists()) {
                String message = Language.getInstance().translate("message." + Cotton.MODID + ".failed_to_crate_folder");
                serverCommandSource.sendError(new TextComponent(String.format(message, exportedVirtualPacks)));
            } else
                //loop through all of the virtual resource packs of the required type.
                for (VirtualResourcePack virtualResourcePack : virtualResourcePacks) {
                    Map<String, Supplier<String>> contents = virtualResourcePack.contents;

                    //write out all of our entries.
                    for (Map.Entry<String, Supplier<String>> entry : contents.entrySet()) {
                        String location = entry.getKey();
                        Supplier<String> stringSupplier = entry.getValue();
                        File outputFile = new File(exportedVirtualPacks.toString() + "/" + location);
                        outputFile.getParentFile().mkdirs();

                        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                            String s = stringSupplier.get();
                            outputStreamWriter.write(s);
                            outputStreamWriter.flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                            String message = Language.getInstance().translate("message." + Cotton.MODID + ".exportvirtual.failed_to_export");
                            serverCommandSource.sendError(new TextComponent(String.format(message, outputFile.getAbsolutePath())));
                            return;
                        }
                    }

                }
            serverCommandSource.sendFeedback(new TranslatableComponent("message." + Cotton.MODID + ".exportvirtual.exported"), true);
        } else {
            System.out.println("we're dedicated.");
        }
    }

    static class ResourceArgumentType implements ArgumentType<ResourceType> {

        static ResourceArgumentType create() {
            return new ResourceArgumentType();
        }

        @Override
        public ResourceType parse(StringReader reader) throws CommandSyntaxException {
            String argument = reader.readUnquotedString();
            if (ResourceType.CLIENT_RESOURCES.getName().equals(argument)) {
                return ResourceType.CLIENT_RESOURCES;
            }
            if (ResourceType.SERVER_DATA.getName().equals(argument)) {
                return ResourceType.SERVER_DATA;
            }
            String message = Language.getInstance().translate("commands." + Cotton.MODID + ".exportvirtual.invalid_input_error_message");
            throw new CommandSyntaxException(INVALID_INPUT, () -> String.format(message, argument));
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return builder.suggest(ResourceType.CLIENT_RESOURCES.getName()).suggest(ResourceType.SERVER_DATA.getName()).buildFuture();
        }

        @Override
        public Collection<String> getExamples() {
            HashSet<String> examples = new HashSet<>();
            examples.add(ResourceType.CLIENT_RESOURCES.getName());
            examples.add(ResourceType.SERVER_DATA.getName());

            return examples;
        }
    }
}
