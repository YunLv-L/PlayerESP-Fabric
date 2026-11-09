package com.yunlv.playeresp.mixin;

import com.yunlv.playeresp.mixinterface.IOptionInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.function.Consumer;

@Mixin(OptionInstance.class)
public class OptionInstanceMixin<T> implements IOptionInstance<T> {

    @Shadow
    private T value;

    @Shadow
    @Final
    private Consumer<T> onValueUpdate;

    @Override
    public void forceSet(T newValue) {
        if (!Minecraft.getInstance().isSameThread()) {
            value = newValue;
            return;
        }
        if (!Objects.equals(value, newValue)) {
            value = newValue;
            onValueUpdate.accept(value);
        }
    }
}