package io.github.cottonmc.cotton;

import io.github.cottonmc.cotton.block.CottonBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.block.BlockColorMapper;

public class CottonClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ColorProviderRegistry.BLOCK.register((block, pos, world, layer) -> {
			BlockColorMapper provider = ColorProviderRegistry.BLOCK.get(Blocks.WATER);
			return provider == null ? -1 : provider.getColor(block, pos, world, layer);
		}, CottonBlocks.STONE_CAULDRON);
	}
}
