package io.github.cottonmc.cotton.mixin;

import io.github.cottonmc.cotton.Cotton;
import io.github.cottonmc.cotton.registry.Splashes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.MainMenuScreen;
import net.minecraft.client.gui.Screen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Environment(EnvType.CLIENT)
@Mixin(MainMenuScreen.class)
public abstract class MainMenuScreenMixin extends Screen {
    //TODO: This is a brute-force approach bypassing vanilla logic. Please do this better in the future. // Pannoniae
    private static Random RANDOM = new Random();

    @Shadow
    private String splashText;
    // TODO get this value from MinecraftClient, possibly without breaking everything and using ugly hacks
    private static Identifier SPLASHES_LOC =
            new Identifier("texts/splashes.txt");


    @Inject(method = "onInitialized", at = @At(value = "RETURN"))
    public void init(CallbackInfo ci) {
        Splashes.loadDefaultSplashes(SPLASHES_LOC);
        splashText = Splashes.getRandomSplash(RANDOM);
        Cotton.logger.info("Splash set to '" + splashText + "'!");
    }
}
