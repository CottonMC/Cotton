package io.github.cottonmc.cotton.mixin;

import io.github.cottonmc.cotton.registry.Splashes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.MainMenuScreen;
import net.minecraft.client.gui.Screen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Environment(EnvType.CLIENT)
@Mixin(MainMenuScreen.class)
public abstract class MainMenuScreenMixin extends Screen {
    //TODO: splash loading changed in the newest snapshot
//    @Shadow @Final
//    private static Random RANDOM;
//    
//    @Shadow
//    private String splashText;
//
//    @Shadow @Final
//    private static Identifier SPLASHES_LOC;
//
//    @Inject(method = "<init>", at = @At("RETURN"))
//    private void init(CallbackInfo ci) {
//        Splashes.loadDefaultSplashes(SPLASHES_LOC);
//        splashText = Splashes.getRandomSplash(RANDOM);
//    }
}
