package com.example.e_koteapplication.ui.inventory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.e_koteapplication.Gun;

import java.util.ArrayList;
import java.util.List;

public class InventoryViewModel extends ViewModel {

    private MutableLiveData<List<Gun>> gunList = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Gun>> getGunList(){
        return gunList;
    }

    public void addGun(Gun gun) {
        List<Gun> currentGunList = gunList.getValue();
        if (currentGunList != null) {
            currentGunList.add(gun);
            gunList.setValue(currentGunList); // Update the LiveData
        }
    }

    public void setGuns(List<Gun> guns){
        gunList.setValue(guns);
    }
}