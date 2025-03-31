package com.example.e_koteapplication.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    // Constructor for ViewModel
    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to Dashboard");
    }

    // LiveData getter method to observe data changes in the fragment/activity
    public LiveData<String> getText() {
        return mText;
    }

    // You can add more methods here to handle data for the dashboard, such as fetching data from a repository
    // Example:
    public void setText(String text) {
        mText.setValue(text);
    }
}