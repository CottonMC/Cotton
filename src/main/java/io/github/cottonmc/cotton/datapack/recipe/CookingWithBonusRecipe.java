package io.github.cottonmc.cotton.datapack.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.cooking.CookingRecipe;
import net.minecraft.recipe.crafting.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.world.loot.LootManager;
import net.minecraft.world.loot.context.LootContext;

import java.util.Collections;
import java.util.List;

import static net.minecraft.util.JsonHelper.*;

/**
 * Implements a generic machine processing recipe with optional loot table for bonus items.
 *
 * Example JSON:
 *
 * {
 *     "type": "c:example",
 *     "ingredient": {
 *         "item": "minecraft:iron_ore"
 *     },
 *     "result": {
 *         "item": "example:iron_dust",
 *         "count": 2
 *     },
 *     "bonus_loot_table": "example:iron_ore_to_dust_bonuses"
 * }
 *
 * @see CrushingRecipe for an example implementation
 */
public abstract class CookingWithBonusRecipe extends CookingRecipe {

	private final Identifier bonusLootTable;

	public CookingWithBonusRecipe(RecipeType<?> type, Identifier id, String group, Ingredient input, ItemStack output, float exp, int cookingTime, Identifier bonusLootTable) {
		super(type, id, group, input, output, exp, cookingTime);
		this.bonusLootTable = bonusLootTable;
	}

	public Identifier getBonusLootTable() {
		return bonusLootTable;
	}

	public List<ItemStack> craftBonus(LootManager lootManager, LootContext lootContext) {
		if (bonusLootTable == null)
			return Collections.emptyList();
		return lootManager.getSupplier(bonusLootTable).getDrops(lootContext);
	}

	@FunctionalInterface
	public interface Factory<R extends CookingWithBonusRecipe> {
		R create(Identifier id, String group, Ingredient input, ItemStack output, float exp, int cookingTime, Identifier bonusLootTable);
	}

	public static class Serializer<R extends CookingWithBonusRecipe> implements RecipeSerializer<R> {

		private final Factory<R> factory;
		private final int defaultCookTime;

		public Serializer(Factory<R> factory, int defaultCookTime) {
			this.factory = factory;
			this.defaultCookTime = defaultCookTime;
		}

		@Override
		public R read(Identifier id, JsonObject jsonObject) {
			String group = getString(jsonObject, "group", "");

			Ingredient input = Ingredient.fromJson(
					hasArray(jsonObject, "ingredient")
							? getArray(jsonObject, "ingredient")
							: getObject(jsonObject, "ingredient")
			);

			ItemStack output = ShapedRecipe.getItemStack(getObject(jsonObject, "result"));
			float exp = getFloat(jsonObject, "experience", 0.0F);
			int cookTime = getInt(jsonObject, "cookingtime", this.defaultCookTime);

			String bonusLoot = getString(jsonObject, "bonus_loot_table", null);
			Identifier bonusLootId = bonusLoot == null ? null : Identifier.create(bonusLoot);

			R r = factory.create(id, group, input, output, exp, cookTime, bonusLootId);
			return r;
		}

		@Override
		public R read(Identifier id, PacketByteBuf buffer) {
			String group = buffer.readString(32767);
			Ingredient input = Ingredient.fromPacket(buffer);
			ItemStack output = buffer.readItemStack();
			float exp = buffer.readFloat();
			int cookTime = buffer.readVarInt();
			Identifier bonusLoot = buffer.readBoolean() ? buffer.readIdentifier() : null;
			return factory.create(id, group, input, output, exp, cookTime, bonusLoot);
		}

		@Override
		public void write(PacketByteBuf buffer, R recipe) {
			buffer.writeString(recipe.group);
			recipe.input.write(buffer);
			buffer.writeItemStack(recipe.output);
			buffer.writeFloat(recipe.experience);
			buffer.writeVarInt(recipe.cookTime);

			Identifier bonusLoot = recipe.getBonusLootTable();
			buffer.writeBoolean(bonusLoot != null);
			if (bonusLoot != null) {
				buffer.writeIdentifier(bonusLoot);
			}
		}
	}
}
