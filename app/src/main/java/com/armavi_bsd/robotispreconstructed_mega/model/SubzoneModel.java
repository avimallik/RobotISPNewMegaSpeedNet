package com.armavi_bsd.robotispreconstructed_mega.model;

public class SubzoneModel {
    private String zoneId;
    private String zoneName;
    private String areaCount;

    public SubzoneModel(String zoneId, String zoneName, String areaCount) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.areaCount = areaCount;
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

    public String getAreaCount() {
        return areaCount;
    }

    public void setAreaCount(String areaCount) {
        this.areaCount = areaCount;
    }
}
