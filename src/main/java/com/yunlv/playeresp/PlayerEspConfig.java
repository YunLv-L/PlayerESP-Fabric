package com.yunlv.playeresp;

public class PlayerEspConfig {
    public boolean enabled = true;
    public int style = 2;
    public int boxSizeMode = 1;
    public boolean filterCreative = true;
    public boolean filterSleeping = true;
    public boolean filterInvisible = true;

    // 药水类型：0 = 喷溅 (Splash), 1 = 滞留 (Lingering)
    public int killPotionType = 0;
    public int trollPotionType = 0;

    // 自定义按键绑定（-1 表示未设置）
    public int keyMenu = 82;       // R
    public int keyChestEsp = 67;   // C
    public int keyItemEsp = 73;    // I
    public int keyMobEsp = 77;     // M
    public int keyNoFall = 78;     // N
    public int keyFullbright = 66; // B

    public boolean hasBoxes() { return style == 0 || style == 2; }
    public boolean hasLines() { return style == 1 || style == 2; }
    public float getExtraSize() { return boxSizeMode == 1 ? 0.1f : 0.0f; }
}