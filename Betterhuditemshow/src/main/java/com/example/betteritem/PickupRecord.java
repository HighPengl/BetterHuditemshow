package com.example.betteritem;

public class PickupRecord {
    private final String formattedName; // 已处理过颜色的名字
    private final int amount;
    private final long timestamp;

    public PickupRecord(String formattedName, int amount) {
        this.formattedName = formattedName;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }

    public String getFormattedName() { return formattedName; }
    public int getAmount() { return amount; }
    public long getTimestamp() { return timestamp; }
}