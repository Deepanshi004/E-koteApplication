package com.example.e_koteapplication.ui.home;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.e_koteapplication.R;
import com.example.e_koteapplication.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Initialize ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Inflate the layout
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Bind text1 from ViewModel to TextView
        final TextView textView1 = binding.homeTitle;
        homeViewModel.getText1().observe(getViewLifecycleOwner(), textView1::setText);

        // Bind text2 from ViewModel to TextView with HTML rendering
        final TextView textView2 = binding.homeContent;
        homeViewModel.getText2().observe(getViewLifecycleOwner(), textView2::setText);

        // Slide-in animation for title (textView1)
        TranslateAnimation slideIn = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,  // Start off-screen (left)
                Animation.RELATIVE_TO_PARENT, 0.0f,   // End at its normal position
                Animation.RELATIVE_TO_PARENT, 0.0f,   // No change in vertical position
                Animation.RELATIVE_TO_PARENT, 0.0f);  // No change in vertical position

        slideIn.setDuration(1000);  // 1 second duration
        slideIn.setInterpolator(new DecelerateInterpolator()); // Smooth deceleration effect
        textView1.startAnimation(slideIn);

        // Slide-in animation for content (textView2)
        TranslateAnimation slideInContent = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,  // Start off-screen (left)
                Animation.RELATIVE_TO_PARENT, 0.0f,   // End at its normal position
                Animation.RELATIVE_TO_PARENT, 0.0f,   // No change in vertical position
                Animation.RELATIVE_TO_PARENT, 0.0f);  // No change in vertical position

        slideInContent.setDuration(1200);  // 1.2 seconds duration for content
        slideInContent.setInterpolator(new DecelerateInterpolator()); // Smooth deceleration effect
        textView2.startAnimation(slideInContent);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
