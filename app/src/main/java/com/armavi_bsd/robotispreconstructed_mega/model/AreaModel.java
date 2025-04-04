package com.armavi_bsd.robotispreconstructed_mega.model;

public class AreaModel {
    String zoneId;
    String zoneName;

    public AreaModel(String zoneId, String zoneName) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
