package io.github.cottonmc.cotton.loot;

import com.google.common.collect.BiMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;
import net.minecraft.world.loot.context.LootContextParameters;
import net.minecraft.world.loot.context.LootContextType;
import net.minecraft.world.loot.context.LootContextTypes;

import java.lang.reflect.Field;
import java.util.Arrays;

public final class CottonLootContextTypes {
	public static final LootContextType MACHINE = new LootContextType.Builder()
			.require(LootContextParameters.BLOCK_ENTITY)
			.require(LootContextParameters.BLOCK_STATE)
			.require(LootContextParameters.POSITION)
			.build();

	@SuppressWarnings("unchecked")
	private static final Lazy<BiMap<Identifier, LootContextType>> REGISTRY = new Lazy<>(() -> {
		try {
			Field field = Arrays.stream(LootContextTypes.class.getDeclaredFields())
					.filter(f -> BiMap.class.isAssignableFrom(f.getType()))
					.findAny()
					.orElseThrow(() -> new RuntimeException("Could not find BiMap field in LootContextTypes"));

			field.setAccessible(true);
			return (BiMap<Identifier, LootContextType>) field.get(null);
		} catch (Exception e) {
			throw new RuntimeException("Could not find LootContextType registry", e);
		}
	});

	public static void init() {
		register(new Identifier("cotton", "machine"), MACHINE);
	}

	private static void register(Identifier id, LootContextType type) {
		REGISTRY.get().put(id, type);
	}
}
