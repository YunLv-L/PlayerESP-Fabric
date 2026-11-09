package com.yunlv.playeresp.feature;

import com.yunlv.playeresp.mixinterface.IOptionInstance;
import net.minecraft.client.Minecraft;

public class FullbrightHandler {

    private static Double originalGamma = null;

    public static void tick(Minecraft mc) {
        if (mc.options == null) return;

        if (FeatureManager.fullbright) {
            if (originalGamma == null) {
                originalGamma = mc.options.gamma().get();
            }
            IOptionInstance.get(mc.options.gamma()).forceSet(16.0);
        } else if (originalGamma != null) {
            IOptionInstance.get(mc.options.gamma()).forceSet(originalGamma);
            originalGamma = null;
        }
    }
}