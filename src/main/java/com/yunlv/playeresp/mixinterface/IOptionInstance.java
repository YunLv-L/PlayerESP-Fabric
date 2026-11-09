package com.yunlv.playeresp.mixinterface;

import net.minecraft.client.OptionInstance;

public interface IOptionInstance<T> {

    void forceSet(T value);

    @SuppressWarnings("unchecked")
    static <T> IOptionInstance<T> get(OptionInstance<T> option) {
        return (IOptionInstance<T>) (Object) option;
    }
}