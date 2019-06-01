package io.github.cottonmc.cotton;

import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.config.CottonConfig;
import io.github.cottonmc.cotton.datapack.PackMetaManager;
import io.github.cottonmc.cotton.datapack.recipe.CottonRecipes;
import io.github.cottonmc.cotton.datapack.recipe.RecipeUtil;
import io.github.cottonmc.cotton.logging.Ansi;
import io.github.cottonmc.cotton.logging.ModLogger;
import io.github.cottonmc.cotton.registry.CommonTags;
import io.github.cottonmc.cotton.tweaker.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.File;

public class Cotton implements ModInitializer {

	public static final String MODID = "cotton";
	public static final String SHARED_NAMESPACE = "c";

	public static final boolean isDevEnv = Boolean.parseBoolean(System.getProperty("fabric.development", "true"));

	public static final File DATA_PACK_LOCATION = new File(FabricLoader.getInstance().getGameDirectory(), "datapacks/cotton_generated");

	public static final ItemGroup commonGroup = FabricItemGroupBuilder.build(
			new Identifier("cotton", "common_tab"), () -> new ItemStack(Blocks.LIGHT_BLUE_WOOL));

	public static final ModLogger logger = new ModLogger(MODID, "COTTON");
	public static CottonConfig config;

    @Override
	public void onInitialize() {
		//setup
		//TODO: uncomment once we have a way to check if a console supports ANSI
//		logger.setPrefixFormat(Ansi.Blue);
		PackMetaManager.saveMeta();
		CottonRecipes.init();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new TweakerLoader());
		Tweaker.addTweaker(CauldronTweaker.INSTANCE);
		Tweaker.addTweaker(RecipeTweaker.INSTANCE);
		TweakerStackGetter.registerGetter(new Identifier("minecraft", "potion"), (id) -> {
			Potion potion = Potion.byId(id.toString());
			if (potion == Potions.EMPTY) return ItemStack.EMPTY;
			return PotionUtil.setPotion(new ItemStack(Items.POTION), potion);
		});

		//example config and logger code
		config = ConfigManager.loadConfig(CottonConfig.class);
		logger.info("Loaded config.");

		//example datapack manager code
		CommonTags.init();
		RecipeUtil.init(config);
//		TagEntryManager.registerToTag(TagType.BLOCK, new Identifier("minecraft:enderman_holdable"), "minecraft:string");
//		TagEntryManager.registerToTag(TagType.BLOCK, new Identifier("minecraft:dragon_immune"), "#minecraft:enderman_holdable");
//		LootTableManager.registerBasicBlockDropTable(new Identifier("minecraft", "dirt"));
//		RecipeUtil.removeRecipe(new Identifier("crafting_table"));

		//example cauldron behavior code - turns a sponge into a wet sponge
//		CauldronBehavior.registerBehavior(
//				(ctx) -> ctx.getStack().getItem() == Items.SPONGE
//						&& FluidTags.WATER.contains(ctx.getFluid())
//						&& ctx.getLevel() == 3
//						&& ctx.getPreviousItems().isEmpty()
//						&& !ctx.getWorld().isClient(),
//				(ctx) -> {
//					PlayerEntity player = ctx.getPlayer();
//					World world = ctx.getWorld();
//					BlockPos pos = ctx.getPos();
//					ItemStack stack = ctx.getStack();
//					if (!player.abilities.creativeMode) {
//						ItemStack sponge = new ItemStack(Items.WET_SPONGE);
//						player.increaseStat(Stats.USE_CAULDRON);
//						stack.subtractAmount(1);
//						if (stack.isEmpty()) {
//							player.setStackInHand(ctx.getHand(), sponge);
//						} else if (!player.inventory.insertStack(sponge)) {
//							player.dropItem(sponge, false);
//						} else if (player instanceof ServerPlayerEntity) {
//							((ServerPlayerEntity)player).method_14204(player.playerContainer);
//						}
//					}
//					ctx.getWorld().playSound(null, ctx.getPos(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCK, 1.0f, 1.0f);
//					((Cauldron)ctx.getState().getBlock()).drain(world, pos, ctx.getState(), Fluids.WATER, 3);
//				}
//		);
	}
}
