package com.example.e_koteapplication.ui.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutViewModel extends ViewModel {

    private final MutableLiveData<String> aboutText = new MutableLiveData<>("About");
    private final MutableLiveData<String> whoWeAreText = new MutableLiveData<>("Who We Are:");
    private final MutableLiveData<String> missionText = new MutableLiveData<>("Details:");
    private final MutableLiveData<String> visionText = new MutableLiveData<>("Vision:");


    public LiveData<String> getAboutText() {
        return aboutText;
    }
    public LiveData<String> getWhoWeAreText() {
        return whoWeAreText;
    }

    public LiveData<String> getMissionText() {
        return missionText;
    }

    public LiveData<String> getVisionText() {
        return visionText;
    }
}