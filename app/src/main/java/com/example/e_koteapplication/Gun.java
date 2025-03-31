package com.example.e_koteapplication;

public class Gun {
    private String id;
    private String name;
    private String model;
    private int quantity;

    public Gun(String id, String name, String model, int quantity) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.quantity = quantity;
    }


    public Gun(){}


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String gunName) {
        this.name = gunName;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String gunModel) {
        this.model = gunModel;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}