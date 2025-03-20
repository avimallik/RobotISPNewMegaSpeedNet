package com.armavi_bsd.robotispreconstructed_mega.model;

public class PackageModel {
    String packageName;
    String netSpeed;
    String billAmount;

    public PackageModel(String packageName, String netSpeed, String billAmount) {
        this.packageName = packageName;
        this.netSpeed = netSpeed;
        this.billAmount = billAmount;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getNetSpeed() {
        return netSpeed;
    }

    public void setNetSpeed(String netSpeed) {
        this.netSpeed = netSpeed;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }
}
