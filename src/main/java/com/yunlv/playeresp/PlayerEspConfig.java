package com.yunlv.playeresp;

public class PlayerEspConfig {
    public boolean enabled = true;
    public int style = 2;
    public int boxSizeMode = 1;
    public boolean filterCreative = true;
    public boolean filterSleeping = true;
    public boolean filterInvisible = true;

    public boolean hasBoxes() { return style == 0 || style == 2; }
    public boolean hasLines() { return style == 1 || style == 2; }
    public float getExtraSize() { return boxSizeMode == 1 ? 0.1f : 0.0f; }
}