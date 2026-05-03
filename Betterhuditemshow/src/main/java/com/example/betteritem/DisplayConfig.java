package com.example.betteritem;

public class DisplayConfig {
    private final String nameFormat;
    private final int pickedDurationSeconds;
    private final int maxRecords;
    private final ColorMode colorMode;

    public DisplayConfig(String nameFormat, int pickedDurationSeconds, int maxRecords, ColorMode colorMode) {
        this.nameFormat = nameFormat;
        this.pickedDurationSeconds = pickedDurationSeconds;
        this.maxRecords = maxRecords;
        this.colorMode = colorMode;
    }

    public String getNameFormat() { return nameFormat; }
    public int getPickedDurationSeconds() { return pickedDurationSeconds; }
    public int getMaxRecords() { return maxRecords; }
    public ColorMode getColorMode() { return colorMode; }

    public enum ColorMode {
        STRIP,        // 删除所有颜色代码
        MINIMESSAGE,  // 转换为 MiniMessage 标签
        NONE;         // 保留原始字符串（含§/&）

        public static ColorMode fromString(String s) {
            if (s == null) return NONE;
            switch (s.toLowerCase()) {
                case "strip": return STRIP;
                case "minimessage": return MINIMESSAGE;
                case "none": return NONE;
                default: return NONE;
            }
        }
    }
}