package com.armavi_bsd.robotispreconstructed_mega.model;

public class ComplainTypeModel {
    String id;
    String template;

    public ComplainTypeModel(String id, String template) {
        this.id = id;
        this.template = template;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
