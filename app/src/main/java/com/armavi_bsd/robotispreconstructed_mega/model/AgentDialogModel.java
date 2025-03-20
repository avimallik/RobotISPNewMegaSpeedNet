package com.armavi_bsd.robotispreconstructed_mega.model;

public class AgentDialogModel {

    private String agId;
    private String agName;
    private String agMobileNumber;
    private String agIp;

    public AgentDialogModel(String agId, String agName, String agMobileNumber, String agIp) {
        this.agId = agId;
        this.agName = agName;
        this.agMobileNumber = agMobileNumber;
        this.agIp = agIp;
    }

    public String getAgId() {
        return agId;
    }
    public String getAgName() {
        return agName;
    }

    public void setAgId(String agId) {
        this.agId = agId;
    }

    public void setAgName(String agName) {
        this.agName = agName;
    }

    public String getAgMobileNumber() {
        return agMobileNumber;
    }

    public void setAgMobileNumber(String agMobileNumber) {
        this.agMobileNumber = agMobileNumber;
    }

    public String getAgIp() {
        return agIp;
    }

    public void setAgIp(String agIp) {
        this.agIp = agIp;
    }
}
