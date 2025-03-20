package com.armavi_bsd.robotispreconstructed_mega.model;

/**
 * Created by Arm_AVI on 2/28/2018.
 */

public class Subjects {

    String SubName = null;
    String SubFullForm = null;

    public Subjects(String Sname, String SFullForm) {

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
