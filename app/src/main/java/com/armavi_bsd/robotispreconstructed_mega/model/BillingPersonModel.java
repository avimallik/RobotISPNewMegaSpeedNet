package com.armavi_bsd.robotispreconstructed_mega.model;

public class BillingPersonModel {
    String UserId;
    String FullName;

    public BillingPersonModel(String userId, String fullName) {
        UserId = userId;
        FullName = fullName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}
