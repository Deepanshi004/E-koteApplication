package com.example.e_koteapplication.ui.maintenance;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_koteapplication.Gun;
import com.example.e_koteapplication.GunMaintenance;
import com.example.e_koteapplication.MaintenanceAdapter;
import com.example.e_koteapplication.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class maintenanceFragment extends Fragment {

    private static final String TAG = "MaintenanceFragment";

    private EditText gunIdEditText, maintenanceDateEditText;
    private Button saveButton, viewHistoryButton;
    private RecyclerView recyclerView;
    private MaintenanceAdapter adapter;
    private List<GunMaintenance> maintenanceList;

    private FirebaseFirestore firestore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maintenance, container, false);

        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);

        gunIdEditText = view.findViewById(R.id.gunId);
        maintenanceDateEditText = view.findViewById(R.id.nextMaintenanceDate);
        saveButton = view.findViewById(R.id.saveMaintenanceDateButton);
        viewHistoryButton = view.findViewById(R.id.viewMaintenanceHistoryButton);
        recyclerView = view.findViewById(R.id.recyclerViewmaintenance);

        maintenanceList = new ArrayList<>();
        adapter = new MaintenanceAdapter(maintenanceList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        saveButton.setOnClickListener(v -> saveMaintenanceDetails());
        viewHistoryButton.setOnClickListener(v -> loadMaintenanceHistory());

        return view;
    }

    private void saveMaintenanceDetails() {
        String gunId = gunIdEditText.getText().toString().trim();
        String maintenanceDate = maintenanceDateEditText.getText().toString().trim();

        if (!gunId.isEmpty() && !maintenanceDate.isEmpty()) {
            GunMaintenance maintenance = new GunMaintenance(gunId, maintenanceDate);
            firestore.collection("GunMaintenance")
                    .add(maintenance)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "Maintenance details saved successfully!", Toast.LENGTH_SHORT).show();
                        gunIdEditText.setText("");
                        maintenanceDateEditText.setText("");
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error saving to Firestore", e);
                        Toast.makeText(getContext(), "Error saving data!", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMaintenanceHistory() {
        maintenanceList.clear();
        firestore.collection("GunMaintenance")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            GunMaintenance maintenance = documentSnapshot.toObject(GunMaintenance.class);
                            if (maintenance != null) {
                                maintenanceList.add(maintenance);
                                fetchGunDetails(maintenance);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "No maintenance history found!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching maintenance records", e));
    }

    private void fetchGunDetails(GunMaintenance maintenance) {
        String gunId = maintenance.getGunId();
        firestore.collection("guns")
                .whereEqualTo("id", gunId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Gun gun = doc.toObject(Gun.class);
                        if (gun != null) {
                            maintenance.setGunName(gun.getName());
                            maintenance.setGunModel(gun.getModel());
                            Log.d(TAG, "Gun details fetched: " + gun.getName() + ", " + gun.getModel());
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error fetching gun details for ID: " + gunId, e));
    }
}
