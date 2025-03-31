package com.example.e_koteapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.*;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BarcodeDetails extends AppCompatActivity {

    private EditText issueIdentifierEditText;
    private ImageView barcodeImageView;
    private FirebaseFirestore firestore;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ListenerRegistration issueListener, gunListener, userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_barcode);

        firestore = FirebaseFirestore.getInstance();
        issueIdentifierEditText = findViewById(R.id.uniqueIdentifierEditText);
        barcodeImageView = findViewById(R.id.barcodeImageView);

        issueIdentifierEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String issueIdentifier = s.toString().trim();
                if (!issueIdentifier.isEmpty()) {
                    setupRealTimeListeners(issueIdentifier);
                } else {
                    Toast.makeText(BarcodeDetails.this, "Please enter a unique identifier", Toast.LENGTH_SHORT).show();
                    barcodeImageView.setImageBitmap(null); // Clear the barcode image if input is cleared
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });
    }

    private void setupRealTimeListeners(String issueIdentifier) {
        if (issueListener != null) issueListener.remove(); // Prevent duplicate listeners

        issueListener = firestore.collection("IssuedReturns")
                .whereEqualTo("issueId", issueIdentifier)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        Log.e("BarcodeDetails", "Error listening to issue updates", error);
                        return;
                    }
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String gunId = document.getString("gunId");
                        String issueDate = document.getString("issueDate");
                        String returnDate = document.getString("returnDate");
                        String userId = document.getString("userId");
                        String status = document.getString("status");

                        Log.d("FirestoreListener", "Issue document updated: " + issueIdentifier);
                        setupGunListener(issueIdentifier, gunId, issueDate, returnDate, userId, status);
                    }
                });
    }

    private void setupGunListener(String issueIdentifier, String gunId, String issueDate, String returnDate, String userId, String status) {
        if (gunListener != null) gunListener.remove(); // Remove old listener if exists

        gunListener = firestore.collection("guns")
                .whereEqualTo("id", gunId)
                .addSnapshotListener((gunSnapshot, e) -> {
                    if (e != null) {
                        Log.e("BarcodeDetails", "Error fetching gun data", e);
                        return;
                    }
                    if (gunSnapshot != null && !gunSnapshot.isEmpty()) {
                        DocumentSnapshot gunDocument = gunSnapshot.getDocuments().get(0);
                        String gunName = gunDocument.getString("name");
                        String gunModel = gunDocument.getString("model");

                        Log.d("FirestoreListener", "Gun document updated: " + gunId);
                        setupUserListener(issueIdentifier, gunId, gunName, gunModel, issueDate, returnDate, userId, status);
                    }
                });
    }

    private void setupUserListener(String issueIdentifier, String gunId, String gunName, String gunModel, String issueDate, String returnDate, String userId, String status) {
        if (userListener != null) userListener.remove(); // Remove old listener if exists

        userListener = firestore.collection("Users")
                .whereEqualTo("idCard", userId)
                .addSnapshotListener((userSnapshot, e) -> {
                    if (e != null) {
                        Log.e("BarcodeDetails", "Error fetching user data", e);
                        return;
                    }
                    if (userSnapshot != null && !userSnapshot.isEmpty()) {
                        DocumentSnapshot userDocument = userSnapshot.getDocuments().get(0);
                        String userName = userDocument.getString("name");
                        String userEmail = userDocument.getString("email");

                        Log.d("FirestoreListener", "User document updated: " + userId);

                        String barcodeData = String.format("Issue Id: %s\nGun Id: %s\nGun Name: %s\nGun Model: %s\nUser Id: %s\nUser Name: %s\nUser Email: %s\nIssue Date: %s\nReturn Date: %s\nStatus: %s",
                                issueIdentifier, gunId, gunName, gunModel, userId, userName, userEmail, issueDate, returnDate, status);

                        generateAndDisplayBarcode(barcodeData); // Automatically update QR code
                    }
                });
    }

    private void generateAndDisplayBarcode(String data) {
        runOnUiThread(() -> barcodeImageView.setImageBitmap(null)); // Clear old QR code first
        executorService.execute(() -> {
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(data, com.google.zxing.BarcodeFormat.QR_CODE, 600, 600);
                runOnUiThread(() -> barcodeImageView.setImageBitmap(bitmap));
                Log.d("BarcodeDetails", "QR Code updated with data: " + data);
            } catch (Exception e) {
                Log.e("BarcodeDetails", "Error generating QR code", e);
                runOnUiThread(() -> Toast.makeText(BarcodeDetails.this, "Error generating QR code", Toast.LENGTH_SHORT).show());
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (issueListener != null) issueListener.remove();
        if (gunListener != null) gunListener.remove();
        if (userListener != null) userListener.remove();
        executorService.shutdown();
    }
}
