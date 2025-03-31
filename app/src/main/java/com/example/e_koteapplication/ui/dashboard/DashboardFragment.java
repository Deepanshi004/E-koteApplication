package com.example.e_koteapplication.ui.dashboard;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e_koteapplication.BarcodeDetails;
import com.example.e_koteapplication.R;

public class DashboardFragment extends Fragment {
    private DashboardViewModel mViewModel;

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        TextView viewGunsTextView = root.findViewById(R.id.Viewguns);
        TextView recordGunsTextView = root.findViewById(R.id.recordguns);
        TextView issuedGunsTextView = root.findViewById(R.id.issuedguns);
        TextView maintenanceTextView = root.findViewById(R.id.maintenance);
        TextView barcodeTextView =root.findViewById(R.id.barcode);
        TextView uniqueidentifier = root.findViewById(R.id.uniqueidgenerator);
        TextView dashboardTextView = root.findViewById(R.id.dashboardtextview);
        ImageView imageView4 = root.findViewById(R.id.imageView4);
        ImageView imageView6 = root.findViewById(R.id.imageView6);
        ImageView imageView5 = root.findViewById(R.id.imageView5);
        ImageView maintenanceimg = root.findViewById(R.id.maintenanceimg);
        ImageView barcodeimg = root.findViewById(R.id.barcodeimg);
        ImageView uniqueimg = root.findViewById(R.id.uniqueimg);


        mViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                dashboardTextView.setText(s);
            }
        });

        NavController navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment_content_main);


        // Set up click listeners for each section
        viewGunsTextView.setOnClickListener(v -> {
            // Navigate to View All Guns screen or fragment
            Log.d("DashboardFragment", "View Guns clicked");
            navController.navigate(R.id.nav_inventory_management);

        });
        imageView4.setOnClickListener(v -> {
            // Navigate to View All Guns screen or fragment
            Log.d("DashboardFragment", "View Guns clicked");
            navController.navigate(R.id.nav_inventory_management);

        });


        issuedGunsTextView.setOnClickListener(v -> {
            Log.d("IssuedReturnFragment", "Fragment View Created");
            navController.navigate(R.id.nav_issued_return);
        });
        imageView6.setOnClickListener(v -> {
            Log.d("IssuedReturnFragment", "Fragment View Created");
            navController.navigate(R.id.nav_issued_return);
        });




        barcodeTextView.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), BarcodeDetails.class);
            startActivity(intent);
        });
        barcodeimg.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), BarcodeDetails.class);
            startActivity(intent);
        });


        recordGunsTextView.setOnClickListener(v -> {
            navController.navigate(R.id.nav_helpline);
        });
       uniqueimg.setOnClickListener(v -> {
            navController.navigate(R.id.nav_helpline);
        });

       uniqueidentifier.setOnClickListener(v -> {
            navController.navigate(R.id.nav_about);
        });
        imageView5.setOnClickListener(v -> {
            navController.navigate(R.id.nav_about);
        });


        maintenanceTextView.setOnClickListener(v -> {
            navController.navigate(R.id.nav_maintenance);
        });
        maintenanceimg.setOnClickListener(v -> {
            navController.navigate(R.id.nav_maintenance);
        });

        return root;
    }
}