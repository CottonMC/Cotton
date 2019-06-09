package io.github.cottonmc.cotton.loot;

import net.minecraft.world.loot.context.LootContextParameters;
import net.minecraft.world.loot.context.LootContextType;

public final class CottonLootContextTypes {
	public static final LootContextType MACHINE = new LootContextType.Builder()
			.require(LootContextParameters.BLOCK_ENTITY)
			.require(LootContextParameters.BLOCK_STATE)
			.require(LootContextParameters.POSITION)
			.build();
}
