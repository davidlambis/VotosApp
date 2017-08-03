package com.votosapp.Models;

public class Zone {

    public long Zone_Id_Local;
    public long Zone_Id;
    public long Type_Zone_id;
    public long City_Id;
    public long Sector_Type_Id;
    public String Name;
    public String Description;
    public double Latitude;
    public double Longitude;

    public Zone(){
        this.Zone_Id_Local = Zone_Id_Local;
        this.Zone_Id = Zone_Id;
        this.Type_Zone_id = Type_Zone_id;
        this.City_Id = City_Id ;
        this.Sector_Type_Id = Sector_Type_Id;
        this.Name = Name;
        this.Description = Description;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    public long getZone_Id_Local() {
        return Zone_Id_Local;
    }

    public void setZone_Id_Local(long zone_Id_Local) {
        Zone_Id_Local = zone_Id_Local;
    }

    public long getZone_Id() {
        return Zone_Id;
    }

    public void setZone_Id(long zone_Id) {
        Zone_Id = zone_Id;
    }

    public long getType_Zone_id() {
        return Type_Zone_id;
    }

    public void setType_Zone_id(long type_Zone_id) {
        Type_Zone_id = type_Zone_id;
    }

    public long getCity_Id() {
        return City_Id;
    }

    public void setCity_Id(long city_Id) {
        City_Id = city_Id;
    }

    public long getSector_Type_Id() {
        return Sector_Type_Id;
    }

    public void setSector_Type_Id(long sector_Type_Id) {
        Sector_Type_Id = sector_Type_Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}