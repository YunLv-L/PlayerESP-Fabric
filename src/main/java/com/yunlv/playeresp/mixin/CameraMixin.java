package com.yunlv.playeresp.mixin;

import com.yunlv.playeresp.feature.FreecamHandler;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow
    private boolean detached;

    @Shadow
    protected void setPosition(Vec3 pos) { throw new AssertionError(); }

    @Shadow
    protected void setRotation(float yaw, float pitch) { throw new AssertionError(); }

    @Inject(
        method = "setup(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;ZZF)V",
        at = @At("RETURN")
    )
    private void onSetup(Level level, Entity entity, boolean bl, boolean bl2, float partialTicks, CallbackInfo ci) {
        if (!FreecamHandler.isActive()) return;
        this.detached = true;
        setPosition(FreecamHandler.getCamPos(partialTicks));
        setRotation(FreecamHandler.getCamYaw(), FreecamHandler.getCamPitch());
    }
}