package io.github.cottonmc.cotton;

import com.google.common.collect.ImmutableMap;
import io.github.cottonmc.cotton.config.ConfigManager;
import io.github.cottonmc.cotton.config.CottonConfig;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

/**
 * Used for loading config-dependent mixins. INTERNAL.
 */
public class CottonMixinPlugin implements IMixinConfigPlugin {
    private static final String PACKAGE = "io.github.cottonmc.cotton.mixin";

    // Using Cotton.config would require loading Fabric's mixins (because of the commonGroup field).
    // That won't be done yet when this class loads.
    private static final CottonConfig CONFIG = ConfigManager.loadConfig(CottonConfig.class);
    private static final ImmutableMap<String, BooleanSupplier> MIXIN_STATES =
        ImmutableMap.of(
                PACKAGE + ".TorchBlockMixin", () -> CONFIG.include_tweaks && CONFIG.enable_custom_torch_placement,
                PACKAGE + ".DispenserBlockMixin", () -> CONFIG.include_tweaks && CONFIG.enable_dispenser_place_blocks,
                PACKAGE + ".SplashScreenMixin", () -> CONFIG.include_tweaks && CONFIG.enable_coloured_loading_bar
        );

    @Override
    public void onLoad(String mixinPackage) {
        if (!mixinPackage.startsWith(PACKAGE)) {
            throw new IllegalArgumentException(
                String.format("Invalid package: Expected %s, found %s", PACKAGE, mixinPackage)
            );
        }
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        BooleanSupplier supplier = MIXIN_STATES.get(mixinClassName);
        // If the supplier is not found, ignore it and return true
        return supplier == null || supplier.getAsBoolean();
    }

    // Boring boilerplate below

    @Override
    public List<String> getMixins() {
        // null = loaded from the JSON
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
