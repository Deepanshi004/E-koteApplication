package com.example.e_koteapplication;

public class GunMaintenance {
    private String gunId;
    private String maintenanceDate;
    private String gunName;
    private String gunModel;

    public GunMaintenance(String gunId, String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
        this.gunId = gunId;
    }

    public GunMaintenance(){}

    public String getGunId() {
        return gunId;
    }

    public void setGunId(String gunId) {
        this.gunId = gunId;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public void setGunName(String gunName) {
        this.gunName = gunName;
    }

    public String getGunName() {
        return gunName;
    }

    public void setGunModel(String gunModel) {
        this.gunModel = gunModel;
    }

    public String getGunModel() {
        return gunModel;
    }
}
