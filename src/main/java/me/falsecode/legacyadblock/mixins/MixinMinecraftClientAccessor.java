package me.falsecode.legacyadblock.mixins;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MinecraftClient.class)
public interface MixinMinecraftClientAccessor {
    @Invoker("doItemUse")
    void rightClick();
}
