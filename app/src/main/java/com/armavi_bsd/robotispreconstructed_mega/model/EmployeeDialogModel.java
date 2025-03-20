package com.armavi_bsd.robotispreconstructed_mega.model;

public class EmployeeDialogModel {
    String id;
    String employee_name;

    public EmployeeDialogModel(String id, String employee_name) {
        this.id = id;
        this.employee_name = employee_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }
}
