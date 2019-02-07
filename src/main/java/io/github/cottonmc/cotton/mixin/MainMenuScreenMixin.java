package io.github.cottonmc.cotton.mixin;

import io.github.cottonmc.cotton.registry.SplashRegistry;
import java.util.Random;
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

@Environment(EnvType.CLIENT)
@Mixin(MainMenuScreen.class)
public abstract class MainMenuScreenMixin extends Screen{
    @Shadow @Final
    private static Random RANDOM;
    
    @Shadow
    private String splashText;
    
    @Shadow @Final
    private static Identifier SPLASHES_LOC;
    
    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    private void init(CallbackInfo callbackInfo){
        SplashRegistry.loadSplashes();
        SplashRegistry.loadDefaultSplashes(SPLASHES_LOC);
        splashText = SplashRegistry.getRandomSplash(RANDOM);
    }
}
