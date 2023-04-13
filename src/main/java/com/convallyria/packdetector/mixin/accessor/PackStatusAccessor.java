package com.convallyria.packdetector.mixin.accessor;

import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ResourcePackStatusC2SPacket.class)
public interface PackStatusAccessor {

    @Accessor
    public ResourcePackStatusC2SPacket.Status getStatus();

}
