package com.armavi_bsd.robotispreconstructed_mega.model;

/**
 * Created by Arm Avi on 12/2/2019.
 */

public class ProfileView {


    String SubFullForm = null;

    public ProfileView(String SFullForm) {

        super();

        this.SubFullForm = SFullForm;

    }


    public String getSubFullForm() {

        return SubFullForm;

    }
    public void setSubFullForm(String name) {

        this.SubFullForm = name;

    }


    @Override
    public String toString() {

        return SubFullForm + "";

    }
}
