package com.votosapp.Models;

public class Department {

    public long Department_Id_Local;
    public int Department_Id;
    public String Name;

    public Department() {
        this.Department_Id_Local = Department_Id_Local;
        this.Department_Id = Department_Id;
        this.Name = Name;
    }

    public long getDepartment_Id_Local() {
        return Department_Id_Local;
    }

    public void setDepartment_Id_Local(long department_Id_Local) {
        Department_Id_Local = department_Id_Local;
    }

    public long getDepartment_Id() {
        return Department_Id;
    }

    public void setDepartment_Id(int department_Id) {
        Department_Id = department_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
