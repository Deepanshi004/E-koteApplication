package com.example.e_koteapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextIDCard,
            editTextMobileNumber, editTextRegisterEmail,
            editTextRegisterPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private TextView textViewLoginPrompt;
    private Spinner role;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextIDCard = findViewById(R.id.editTextIDCardNo);
        editTextMobileNumber = findViewById(R.id.editTextMobile);
        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLoginPrompt = findViewById(R.id.textViewlogin);
        role = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(adapter);

        textViewLoginPrompt.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

            buttonRegister.setOnClickListener(v -> {
                String username = editTextName.getText().toString();
                String idCard = editTextIDCard.getText().toString();
                String mobileNumber = editTextMobileNumber.getText().toString();
                String email = editTextRegisterEmail.getText().toString();
                String password = editTextRegisterPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                String roles = role.getSelectedItem().toString();


                if (username.isEmpty() || idCard.isEmpty() || mobileNumber.isEmpty() || email.isEmpty() || password.isEmpty() || !password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please fill the required fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length()<8){
                    Toast.makeText(RegisterActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)){
                    Toast.makeText(RegisterActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(RegisterActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (roles.isEmpty() || roles.equals("Select a role")) {
                    Toast.makeText(this, "Please select a valid role", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(username,idCard,mobileNumber,email,password,roles);
            });
    }

    private void registerUser(String username, String idCard, String mobileNumber, String email, String password, String roles) {
        firestore.collection("Users")
                .whereEqualTo("idCard",idCard)
                .get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()) {
                       if (!task.getResult().isEmpty()) {
                           Toast.makeText(RegisterActivity.this, "ID Card number already exists. Please use a unique Id Card Number.", Toast.LENGTH_SHORT).show();
                       } else {
                           createFirebaseUser(username, idCard, mobileNumber, email, password, roles);
                       }
                   }
                   else {
                           Toast.makeText(RegisterActivity.this, "Error checking ID Card", Toast.LENGTH_SHORT).show();
                       }
                });
    }
    public void createFirebaseUser(String username, String idCard,String mobileNumber,String email,String password,String roles){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();

                    Map<String, Object> userData = new HashMap<>();
                    userData.put("name", username);
                    userData.put("idCard", idCard);
                    userData.put("mobileNumber", mobileNumber);
                    userData.put("email", email);
                    userData.put("password", password);
                    userData.put("roles", roles);

                    firestore.collection("Users").document(userId).set(userData)
                            .addOnCompleteListener(firestoreTask -> {
                        if (firestoreTask.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to save user data" + firestoreTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            else {
                Toast.makeText(RegisterActivity.this, "Registration failed" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}