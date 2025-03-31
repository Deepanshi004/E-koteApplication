package com.example.e_koteapplication.ui.about;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.e_koteapplication.R;
import com.example.e_koteapplication.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAboutBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.about.setText("About us");
        binding.p1.setText("At E-KOTE, we believe in enhancing the efficiency and security of weapon management systems. Our barcode-based tracking system enables real-time monitoring, ensuring accurate inventory and the highest levels of accountability.");
        binding.p2.setText("Our mission is to replace outdated manual record-keeping processes with a modern, automated system that tracks weapons, their usage, maintenance, and history. Through this innovative approach, we aim to reduce errors and provide a robust, secure weapon management system.");
        binding.h3.setText("Vision:");
        binding.p3.setText("Our vision is to revolutionize the weapon management system in defense services by providing a secure and transparent way to manage every weapon issued, cleaned, or maintained within the defense infrastructure.");

        // If you have LiveData in ViewModel for each text, observe here:
        AboutViewModel aboutViewModel = new ViewModelProvider(this).get(AboutViewModel.class);



        aboutViewModel.getAboutText().observe(getViewLifecycleOwner(), binding.about::setText);
        aboutViewModel.getMissionText().observe(getViewLifecycleOwner(), binding.h2::setText);
        aboutViewModel.getVisionText().observe(getViewLifecycleOwner(), binding.h3::setText);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}