package com.example.e_koteapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;


public class LoginActivity extends AppCompatActivity {

        private EditText editTextEmail, editTextPassword;
        private Button buttonLogin;
        private TextView textViewRegister;
        private FirebaseFirestore firestore;

        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            FirebaseAuth auth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();

            editTextEmail = findViewById(R.id.editTextEmail);
            editTextPassword = findViewById(R.id.editTextPassword);
            buttonLogin = findViewById(R.id.buttonLogin);
            textViewRegister = findViewById(R.id.textViewRegister);

            textViewRegister.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            });

            buttonLogin.setOnClickListener(v -> {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || password.isEmpty()) {
                    // Show error message
                    Toast.makeText(LoginActivity.this, "Please fill the required fields", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()){
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user != null) {
                                        fetchUserRole(user.getUid());
                                    }
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Authentication Failed" + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
        private void fetchUserRole(String userId){
            Log.d("LoginActivity", "Fetching user role for userId: " + userId);
            firestore.collection("Users").document(userId)
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                            String roles = document.getString("roles");
                                Log.d("LoginActivity", "Fetched roles: " + roles);
                            if (roles!=null){
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("isLoggedIn",true);
                                intent.putExtra("roles",roles);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this, "Invalid role assigned.", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(LoginActivity.this, "User role not found.", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                            Log.e("LoginActivity", "Failed to fetch user role", task.getException());
                           Toast.makeText(LoginActivity.this, "Failed to fetch role: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

}
