package io.github.cottonmc.cotton.cauldron;

import io.github.cottonmc.cotton.cauldron.tweaker.CauldronTweaker;
import io.github.cottonmc.libcd.api.LibCDInitializer;
import io.github.cottonmc.libcd.api.condition.ConditionManager;
import io.github.cottonmc.libcd.api.tweaker.TweakerManager;
import net.fabricmc.api.ModInitializer;

public class CottonCauldron implements ModInitializer, LibCDInitializer {
    @Override
    public void onInitialize() {
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

    @Override
    public void initTweakers(TweakerManager tweakerManager) {
        tweakerManager.addTweaker("cotton.cauldron.CauldronTweaker", CauldronTweaker.INSTANCE);
    }

    @Override
    public void initConditions(ConditionManager conditionManager) {

    }
}
