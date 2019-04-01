package io.github.cottonmc.cotton;

import io.github.cottonmc.cotton.block.CottonBlocks;
import io.github.cottonmc.cotton.cauldron.CauldronBehavior;
import io.github.cottonmc.cotton.cauldron.CauldronUtils;
import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.config.CottonConfig;
import io.github.cottonmc.cotton.datapack.recipe.CottonRecipes;
import io.github.cottonmc.cotton.impl.BucketFluidAccessor;
import io.github.cottonmc.cotton.logging.Ansi;
import io.github.cottonmc.cotton.logging.ModLogger;
import io.github.cottonmc.cotton.datapack.PackMetaManager;
import io.github.cottonmc.cotton.registry.CommonTags;
import io.github.cottonmc.cotton.util.FluidProperty;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.File;

public class Cotton implements ModInitializer {

	public static final String MODID = "cotton";
	public static final String SHARED_NAMESPACE = "c";

	public static final boolean isDevEnv = Boolean.parseBoolean(System.getProperty("fabric.development", "false"));

	public static final File DATA_PACK_LOCATION = new File(FabricLoader.getInstance().getGameDirectory(), "datapacks/cotton (generated)/");

	public static final ItemGroup commonGroup = FabricItemGroupBuilder.build(
			new Identifier("cotton", "common_tab"), () -> new ItemStack(Blocks.LIGHT_BLUE_WOOL));

	public static final ModLogger logger = new ModLogger(MODID, "COTTON");
	public static CottonConfig config;

	@Override
	public void onInitialize() {
		//setup
		logger.setPrefixFormat(Ansi.Blue);
		PackMetaManager.saveMeta();
		CottonBlocks.init();
		CottonRecipes.init();

		//example config and logger code
		config = ConfigManager.loadConfig(CottonConfig.class);
		logger.info("Loaded config.");

		//example datapack manager code
		CommonTags.init();
//		TagEntryManager.registerToTag(TagType.BLOCK, new Identifier("minecraft:enderman_holdable"), "minecraft:string");
//		TagEntryManager.registerToTag(TagType.BLOCK, new Identifier("minecraft:dragon_immune"), "#minecraft:enderman_holdable");
//		LootTableManager.registerBasicBlockDropTable(new Identifier("minecraft", "dirt"));
//		RecipeUtil.removeRecipe(new Identifier("crafting_table"));

		//example cauldron behavior code - lets you fill a stone cauldron with fluid
		CauldronBehavior.registerBehavior(
				(ctx) -> ctx.getStack().getItem() instanceof BucketItem
						&& ctx.getStack().getItem() != Items.BUCKET
						&& CauldronUtils.canPlaceFluid(ctx.getState(), new FluidProperty.Wrapper(((BucketFluidAccessor)ctx.getStack().getItem()).cotton_getFluid()))
						&& ctx.getCauldronLevel() == 3 && !ctx.getWorld().isClient(),
				(ctx) -> {
					PlayerEntity player = ctx.getPlayer();
					World world = ctx.getWorld();
					BlockPos pos = ctx.getPos();
					player.increaseStat(Stats.USE_CAULDRON);
					FluidProperty.Wrapper fluid = new FluidProperty.Wrapper(((BucketFluidAccessor)ctx.getStack().getItem()).cotton_getFluid());
					CauldronUtils.placeFluid(world, pos, ctx.getState(), 3, fluid);
					if (!player.abilities.creativeMode) player.setStackInHand(ctx.getHand(), new ItemStack(Items.BUCKET));
					ctx.getWorld().playSound(null, ctx.getPos(), fluid.getFluid() == Fluids.LAVA ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCK, 1.0f, 1.0f);
				}
		);
	}
}
