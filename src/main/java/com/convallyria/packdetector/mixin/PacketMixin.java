package com.convallyria.packdetector.mixin;

import com.convallyria.packdetector.mixin.accessor.PackStatusAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class PacketMixin {

    @Inject(at = @At(value = "HEAD"),
            method = "handlePacket")
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
        if (packet instanceof ResourcePackSendS2CPacket) {
            ResourcePackSendS2CPacket pack = (ResourcePackSendS2CPacket) packet;
            MinecraftClient.getInstance().execute(() -> {
                final ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player == null) return;
                player.sendMessage(Text.of("Received resource pack: " + pack.getURL()), false);
                player.sendMessage(Text.of("Hash: " + pack.getSHA1()), false);
//                if (pack.getPrompt() != null) {
//                    player.sendMessage(Text.literal("Prompt: " + pack.getPrompt().toString()));
//                }
            });
        }
    }

    @Inject(at = @At(value = "HEAD"),
            method = "send(Lnet/minecraft/network/Packet;)V")
    public void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        if (packet instanceof ResourcePackStatusC2SPacket) {
            ResourcePackStatusC2SPacket status = (ResourcePackStatusC2SPacket) packet;
            PackStatusAccessor accessor = (PackStatusAccessor) status;
            MinecraftClient.getInstance().execute(() -> {
                final ClientPlayerEntity player = MinecraftClient.getInstance().player;
                if (player == null) return;
                player.sendMessage(Text.of("Responded with status: " + accessor.getStatus()), false);
            });
        }
    }
}
