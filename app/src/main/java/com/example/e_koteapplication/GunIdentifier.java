package com.example.e_koteapplication;

public class GunIdentifier {
    private String uniqueIdentifier;
    private String manufacturingDate;
    private String manufacturingPlace;
    private String gunId;

    public GunIdentifier(){}

    public GunIdentifier(String uniqueIdentifier, String manufacturingDate, String manufacturingPlace) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.manufacturingDate = manufacturingDate;
        this.manufacturingPlace = manufacturingPlace;
    }

    public void setGunId(String gunId) {
        this.gunId = gunId;
    }

    public String getGunId() {
        return gunId;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {this.manufacturingDate = manufacturingDate;}

    public String getManufacturingPlace() {
        return manufacturingPlace;
    }

    public void setManufacturingPlace(String manufacturingPlace) {this.manufacturingPlace = manufacturingPlace;}
}

