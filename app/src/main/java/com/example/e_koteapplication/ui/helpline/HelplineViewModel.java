package com.example.e_koteapplication.ui.helpline;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HelplineViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HelplineViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Helpline & Support");
    }

    public LiveData<String> getText() {
        return mText;
    }
}