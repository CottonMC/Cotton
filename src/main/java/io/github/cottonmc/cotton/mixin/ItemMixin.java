package io.github.cottonmc.cotton.mixin;

import io.github.cottonmc.cotton.interfaces.IChangeItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Item.class)
public abstract class ItemMixin implements IChangeItemGroup {

    @Shadow
    protected ItemGroup itemGroup;

    /**We want to be able to change the tabs of items that are registered by CommonItems.
     */
    @Override
    public void setItemGroup(ItemGroup tab) {
        this.itemGroup=tab;
    }
}
