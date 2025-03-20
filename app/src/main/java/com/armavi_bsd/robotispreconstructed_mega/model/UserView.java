package com.armavi_bsd.robotispreconstructed_mega.model;

/**
 * Created by Arm Avi on 11/4/2019.
 */

public class UserView {

    String SubName = null;
    String SubFullForm = null;

    public UserView(String Sname, String SFullForm) {

        super();

        this.SubName = Sname;

        this.SubFullForm = SFullForm;

    }

    public String getSubName() {

        return SubName;

    }
    public void setSubName(String code) {

        this.SubName = code;

    }
    public String getSubFullForm() {

        return SubFullForm;

    }
    public void setSubFullForm(String name) {

        this.SubFullForm = name;

    }


    @Override
    public String toString() {

        return  SubName + " " + SubFullForm;

    }
}
