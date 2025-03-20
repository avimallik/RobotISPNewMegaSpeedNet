package com.armavi_bsd.robotispreconstructed_mega.model;

public class Mikrotik {

    private String id;
    private String mikIp;

    public Mikrotik(String id, String mikIp) {
        this.id = id;
        this.mikIp = mikIp;
    }

    public String getId() {
        return id;
    }

    public String getMikIp() {
        return mikIp;
    }

}
