package io.github.cottonmc.cotton.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TorchBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.ViewableWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/* Allows placing torches on more blocks by checking their collision shape
* if they can't be placed otherwise. (not including wall torches) */
@Mixin(TorchBlock.class)
public class TorchBlockMixin {
    // This is as wide as a fence post and 0.5/16 blocks tall.
    private static final BoundingBox TOP_BOX = Block.createCuboidShape(6, 15.5, 6, 10, 16, 10).getBoundingBox();

    /**
     * Checks if the inner BoundingBox is contained in any of the
     * BoundingBoxes of the outer VoxelShape.
     */
    private static boolean containedIn(VoxelShape outer, BoundingBox inner) {
        return outer.getBoundingBoxList().stream().anyMatch(box ->
            box.minX <= inner.minX && box.maxX >= inner.maxX &&
                box.minY <= inner.minY && box.maxY >= inner.maxY &&
                box.minZ <= inner.minZ && box.maxZ >= inner.maxZ
        );
    }

    @Inject(method = "canPlaceAt", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void onCanPlaceAt(BlockState state, ViewableWorld world, BlockPos pos, CallbackInfoReturnable<Boolean> info,
                              BlockPos downPos, BlockState downState, Block downBlock) {
        if (!info.getReturnValue()) {
            info.setReturnValue(containedIn(downState.getCollisionShape(world, downPos), TOP_BOX));
        }
    }
}
