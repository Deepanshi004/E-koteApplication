package com.example.e_koteapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ViewTypeHeader = 0;
    private static final int ViewTypeItem = 1;

    private List<Issue> issueList;
    private final OnDeleteClickListener deleteClickListener;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public interface OnDeleteClickListener {
        void onDeleteClick(Issue issue, int position);
    }

    public IssueAdapter(List<Issue> issueList, OnDeleteClickListener deleteClickListener) {
        this.issueList = issueList;
        this.deleteClickListener = deleteClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ViewTypeHeader : ViewTypeItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ViewTypeHeader) {
            View headerView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_issueheader, parent, false);
            return new HeaderViewHolder(headerView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_issue, parent, false);
            return new ItemViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            Issue issue = issueList.get(position - 1); // Subtract 1 because position 0 is the header
            ((ItemViewHolder) holder).bind(issue, deleteClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return issueList.size() + 1; // +1 for the header view
    }

    // ViewHolder for the Header
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize header views if needed
        }
    }

    // ViewHolder for the Issue Items
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView gunIdText, userIdText, IssueIdText, issueDateText, returnDateText, statusTextView;
        ImageView buttonIssueDelete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            gunIdText = itemView.findViewById(R.id.gunIdText);
            userIdText = itemView.findViewById(R.id.userIdText);
            IssueIdText = itemView.findViewById(R.id.uniquegunIdText);
            issueDateText = itemView.findViewById(R.id.issueDateText);
            returnDateText = itemView.findViewById(R.id.returnDateText);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            buttonIssueDelete = itemView.findViewById(R.id.buttonissueDelete);
        }

        public void bind(Issue issue, OnDeleteClickListener deleteClickListener) {
            gunIdText.setText(issue.getGunId());
            userIdText.setText(issue.getUserId());
            IssueIdText.setText(issue.getIssueId());
            issueDateText.setText(issue.getIssueDate());
            returnDateText.setText(issue.getReturnDate());
            statusTextView.setText(issue.getStatus());

            buttonIssueDelete.setOnClickListener(v -> {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(issue, getAdapterPosition());
                }
            });
        }
    }

    public void removeIssue(int position) {
        Issue issue = issueList.get(position - 1); // Adjust for header
        String issueId = issue.getIssueId();
        String gunId = issue.getGunId();

        if ("Issued".equalsIgnoreCase(issue.getStatus())) {
            firestore.collection("Stock").document(gunId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Long stock = documentSnapshot.getLong("stock");
                            if (stock != null) {
                                firestore.collection("Stock").document(gunId)
                                        .update("stock", stock + 1)
                                        .addOnSuccessListener(aVoid -> deleteIssueFromFirestore(issueId, position))
                                        .addOnFailureListener(e -> {
                                            // Handle failure in updating stock
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure in fetching stock details
                    });
        } else {
            deleteIssueFromFirestore(issueId, position);
        }
    }

    private void deleteIssueFromFirestore(String issueId, int position) {
        firestore.collection("IssuedReturns").document(issueId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    issueList.remove(position - 1); // Adjust for header
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, issueList.size());
                })
                .addOnFailureListener(e -> {
                    // Handle failure (e.g., show a toast message)
                });
    }
}
