package com.example.e_koteapplication.ui.issuedreturn;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.e_koteapplication.Issue;
import com.example.e_koteapplication.IssueAdapter;
import com.example.e_koteapplication.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class IssuedReturnFragment extends Fragment {

    private EditText gunIdIssued, userIdIssued;
    private Button issueBtn, returnBtn, viewDetailsButton;
    private RecyclerView recyclerView;
    private IssueAdapter issueAdapter;
    private List<Issue> issueList;
    private FirebaseFirestore firestore;

    public static IssuedReturnFragment newInstance() {
        return new IssuedReturnFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_issuedreturn, container, false);

        gunIdIssued = root.findViewById(R.id.gunIdissued);
        userIdIssued = root.findViewById(R.id.userIdissued);
        issueBtn = root.findViewById(R.id.Issuegunbtn);
        returnBtn = root.findViewById(R.id.Returngunbtn);
        viewDetailsButton = root.findViewById(R.id.viewReturnHistory);
        recyclerView = root.findViewById(R.id.recyclerViewissue);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        issueList = new ArrayList<>();
        issueAdapter = new IssueAdapter(issueList, (issue, position) -> {
            issueAdapter.removeIssue(position);
            Toast.makeText(getContext(), "Issue deleted successfully", Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(issueAdapter);

        recyclerView.setVisibility(View.GONE);
        viewDetailsButton.setOnClickListener(v -> {
            String gunId = gunIdIssued.getText().toString().trim();
            String userId = userIdIssued.getText().toString().trim();

            if (TextUtils.isEmpty(gunId) && TextUtils.isEmpty(userId)) {
                fetchAllIssues();
            } else {
                fetchFilteredIssues(gunId, userId);
            }
        });

        issueBtn.setOnClickListener(v -> issueGun());
        returnBtn.setOnClickListener(v -> returnGun());

        return root;
    }

    private void returnGun() {
        String gunId = gunIdIssued.getText().toString().trim();
        String userId = userIdIssued.getText().toString().trim();

        if (gunId.isEmpty() || userId.isEmpty()) {
            Toast.makeText(getContext(), "Please enter Gun ID and User ID", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference issuesRef = db.collection("IssuedReturns");

        issuesRef.whereEqualTo("gunId", gunId)
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", "Issued")
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            String documentId = document.getId();
                            String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                            // Update the status to "Returned" and set the return date
                            document.getReference().update("status", "Returned", "returnDate", currentDate)
                                    .addOnSuccessListener(aVoid -> {
                                        // Add to "Returns" collection
                                        Issue returnIssue = new Issue(gunId, userId,document.getString("issueId"), document.getString("issueDate"), currentDate, "Returned");
                                        db.collection("Returns").add(returnIssue);

                                        // Restore gun stock
                                        restoreGunStock(gunId);

                                        // Refresh UI
                                        fetchAllIssues();
                                        Toast.makeText(getContext(), "Gun returned successfully!", Toast.LENGTH_SHORT).show();
                                        gunIdIssued.setText("");
                                        userIdIssued.setText("");
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to return gun: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(getContext(), "No issued record found for the provided Gun ID and User ID", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }


    private void fetchAllIssues() {
        if (!isAdded()) return;

        recyclerView.setVisibility(View.VISIBLE);
        issueList.clear();
        issueAdapter.notifyDataSetChanged();

        firestore.collection("IssuedReturns")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!isAdded()) return;

                    if (querySnapshot.isEmpty()) {
                        Toast.makeText(requireContext(), "No records found", Toast.LENGTH_SHORT).show();
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        issueList.clear();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Issue issue = document.toObject(Issue.class);
                            if (issue != null) {
                                issue.setIssueId(document.getId());                                issueList.add(issue);
                            }
                        }
                        issueAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error loading issues", e));
    }

    private void issueGun() {
        String gunId = gunIdIssued.getText().toString().trim();
        String userId = userIdIssued.getText().toString().trim();

        if (TextUtils.isEmpty(gunId) || TextUtils.isEmpty(userId)) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        firestore.collection("guns").document(gunId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!isAdded()) return;
                    if (documentSnapshot.exists()) {
                        Long stock = documentSnapshot.getLong("quantity");
                        if (stock == null || stock <= 0) {
                            Toast.makeText(requireContext(), "Gun is out of stock", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String issueDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                        String issueId = gunId+userId+UUID.randomUUID().toString().substring(0,2);
                        Issue issueObj = new Issue(gunId, userId,issueId, issueDate, "   ", "Issued");

                        // Directly add the issue without checking for duplicates
                        firestore.collection("IssuedReturns")
                                .document(issueId)
                                .set(issueObj)
                                .addOnSuccessListener(aVoid -> {
                                    updateInventoryStock(gunId, stock - 1);
                                    Toast.makeText(requireContext(), "Gun issued successfully", Toast.LENGTH_SHORT).show();
                                    clearFields();
                                    fetchAllIssues();
                                })
                                .addOnFailureListener(e -> Log.e("Firestore", "Failed to issue gun", e));
                    } else {
                        Toast.makeText(requireContext(), "Invalid Gun ID", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching gun details", e));
    }


    private void fetchFilteredIssues(String gunId, String userId) {
        if (!isAdded()) return;

        recyclerView.setVisibility(View.VISIBLE);
        issueList.clear();
        issueAdapter.notifyDataSetChanged();

        firestore.collection("IssuedReturns")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!isAdded()) return;

                    issueList.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Issue issue = document.toObject(Issue.class);
                        if (issue != null) {
                            boolean matchGunId = TextUtils.isEmpty(gunId) || gunId.equals(issue.getGunId());
                            boolean matchUserId = TextUtils.isEmpty(userId) || userId.equals(issue.getUserId());

                            if (matchGunId && matchUserId) {
                                issueList.add(issue);
                            }
                        }
                    }

                    if (issueList.isEmpty()) {
                        Toast.makeText(requireContext(), "No matching records found", Toast.LENGTH_SHORT).show();
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        issueAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error loading filtered issues", e));
    }




    private void updateInventoryStock(String gunId, long newStock) {
        firestore.collection("guns").document(gunId)
                .update("quantity", newStock)
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to update inventory", e));
    }

    private void restoreGunStock(String gunId) {
        firestore.collection("guns").document(gunId)
                .update("quantity", FieldValue.increment(1));
    }

    private void clearFields() {
        gunIdIssued.setText("");
        userIdIssued.setText("");
    }
}
