package com.votosapp.Models;

public class City {

    public long City_Id_Local;
    public int City_Id;
    public long Department_Id;
    public String Name;

    public City() {
        this.City_Id_Local = City_Id_Local;
        this.City_Id = City_Id;
        this.Department_Id = Department_Id;
        this.Name = Name;
    }

    public long getCity_Id_Local() {
        return City_Id_Local;
    }

    public void setCity_Id_Local(long city_Id_Local) {
        City_Id_Local = city_Id_Local;
    }

    public long getCity_Id() {
        return City_Id;
    }

    public void setCity_Id(int city_Id) {
        City_Id = city_Id;
    }

    public long getDepartment_Id() {
        return Department_Id;
    }

    public void setDepartment_Id(long department_Id) {
        Department_Id = department_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

