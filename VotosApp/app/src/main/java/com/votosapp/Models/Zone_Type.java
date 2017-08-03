package com.votosapp.Models;

public class Zone_Type {

    public long Type_Zone_Id_Local;
    public long Type_Zone_id;
    public String Name;

    public Zone_Type (){
        this.Type_Zone_Id_Local = Type_Zone_Id_Local;
        this.Type_Zone_id = Type_Zone_id;
        this.Name = Name;
    }

    public long getType_Zone_Id_Local() {
        return Type_Zone_Id_Local;
    }

    public void setType_Zone_Id_Local(long type_Zone_Id_Local) {
        Type_Zone_Id_Local = type_Zone_Id_Local;
    }

    public long getType_Zone_id() {
        return Type_Zone_id;
    }

    public void setType_Zone_id(long type_Zone_id) {
        Type_Zone_id = type_Zone_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
