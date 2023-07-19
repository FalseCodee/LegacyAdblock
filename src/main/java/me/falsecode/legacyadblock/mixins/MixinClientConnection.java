package me.falsecode.legacyadblock.mixins;

import me.falsecode.legacyadblock.utils.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(ClientConnection.class)
public class MixinClientConnection {

    @Unique
    private static final Timer timer = new Timer();
    @Unique
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    @Inject(method = "handlePacket", at = @At(value = "HEAD"), cancellable = true)
    private static  <T extends PacketListener> void onPacket(Packet<T> packet, PacketListener listener, CallbackInfo ci){
        if(packet instanceof GameMessageS2CPacket p) {
            String message = concatArray(p.content().withoutStyle(), "");
            if (message.split(" ")[0].toUpperCase().contains("[AD]")) {
                ci.cancel();
            }
        } else if (packet instanceof BossBarS2CPacket p) {
            p.accept(new BossBarS2CPacket.Consumer() {
                @Override
                public void add(UUID uuid, Text name, float percent, BossBar.Color color, BossBar.Style style, boolean darkenSky, boolean dragonMusic, boolean thickenFog) {
                    BossBarS2CPacket.Consumer.super.add(uuid, name, percent, color, style, darkenSky, dragonMusic, thickenFog);
                }

                @Override
                public void remove(UUID uuid) {
                    BossBarS2CPacket.Consumer.super.remove(uuid);
                }

                @Override
                public void updateProgress(UUID uuid, float percent) {
                    BossBarS2CPacket.Consumer.super.updateProgress(uuid, percent);
                }

                @Override
                public void updateName(UUID uuid, Text name) {
                    if(mc.player != null && concatArray(name.withoutStyle(), "").startsWith("Sending you to ") && timer.hasTimeElapsed(100, true)) {
                        mc.player.getInventory().selectedSlot = 2;
                        ((MixinMinecraftClientAccessor)mc).rightClick();
                    }
                    BossBarS2CPacket.Consumer.super.updateName(uuid, name);
                }

                @Override
                public void updateStyle(UUID id, BossBar.Color color, BossBar.Style style) {
                    BossBarS2CPacket.Consumer.super.updateStyle(id, color, style);
                }

                @Override
                public void updateProperties(UUID uuid, boolean darkenSky, boolean dragonMusic, boolean thickenFog) {
                    BossBarS2CPacket.Consumer.super.updateProperties(uuid, darkenSky, dragonMusic, thickenFog);
                }
            });
        }
    }

    @Unique
    private static String concatArray(List<Text> array, String concat) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.size(); i++) {
            sb.append(array.get(i).getString());
            if(i < array.size() - 1) {
                sb.append(concat);
            }
        }

        return sb.toString();
    }
}
