package com.example.e_koteapplication.ui.inventory;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e_koteapplication.Gun;
import com.example.e_koteapplication.GunsAdapter;
import com.example.e_koteapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InventoryFragment extends Fragment {

    private EditText TextGunid, TextGunname, TextGunmodel, TextTextstock;
    private RecyclerView recyclerView;
    private GunsAdapter gunadapter;
    private FirebaseFirestore db;
    private CollectionReference gunsCollection;

    public InventoryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_inventory, container, false);

        TextGunid = rootview.findViewById(R.id.textView11);
        TextGunname = rootview.findViewById(R.id.textGunName);
        TextGunmodel = rootview.findViewById(R.id.TextGunModel);
        TextTextstock = rootview.findViewById(R.id.editTextStock);
        recyclerView = rootview.findViewById(R.id.recyclerViewGuns);
        Button buttonAddGun = rootview.findViewById(R.id.buttonAddGun);
        Button buttonViewGuns = rootview.findViewById(R.id.buttonViewGuns);

        db = FirebaseFirestore.getInstance();
        gunsCollection = db.collection("guns");

        InventoryViewModel inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);

        List<Gun> gunList = new ArrayList<>();
        gunadapter = new GunsAdapter(gunList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(gunadapter);


        buttonAddGun.setOnClickListener(v -> addGunToFirestore());
        buttonViewGuns.setOnClickListener(v -> fetchGunsFromFirestore());

        return rootview;
    }

    private void fetchGunsFromFirestore() {
        gunsCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        List<Gun> gunList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Gun gun = document.toObject(Gun.class);
                            gunList.add(gun);
                        }
                        gunadapter.updateGuns(gunList);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(requireContext(), "Failed to fetch guns", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addGunToFirestore() {
        String gunId= TextGunid.getText().toString().trim();
        String gunName = TextGunname.getText().toString().trim();
        String gunModel = TextGunmodel.getText().toString().trim();
        String quantity = TextTextstock.getText().toString().trim();

        if (gunId.isEmpty() || gunName.isEmpty() || gunModel.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill the required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock;
        try {
            stock = Integer.parseInt(quantity);
        } catch (NumberFormatException e){
            Toast.makeText(requireContext(), "Quantity must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Gun object and add it to the list


        gunsCollection.document(gunId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Toast.makeText(requireContext(), "Gun ID already exists. Please use unique Id", Toast.LENGTH_SHORT).show();
                    } else {
                        Gun newGun = new Gun(gunId, gunName, gunModel, stock);
                        gunsCollection.document(gunId).set(newGun)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(requireContext(), "Gun Added Successfully", Toast.LENGTH_SHORT).show();
                                    clearFields();
                                }).addOnFailureListener(e -> {
                                    Toast.makeText(requireContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to check Gun ID", Toast.LENGTH_SHORT).show();
                });

    }

    private void clearFields() {
        TextGunid.setText("");
        TextGunname.setText("");
        TextGunmodel.setText("");
        TextTextstock.setText("");
    }
}