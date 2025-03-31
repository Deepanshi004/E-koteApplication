package com.example.e_koteapplication;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IdentifierAdapter extends RecyclerView.Adapter<IdentifierAdapter.IdentifierViewHolder> {

    private final List<GunIdentifier> gunIdentifiers;

    public IdentifierAdapter(List<GunIdentifier> gunIdentifiers) {
        this.gunIdentifiers = gunIdentifiers;
    }

    @NonNull
    @Override
    public IdentifierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_identifier, parent, false);
        return new IdentifierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IdentifierViewHolder holder, int position) {
        GunIdentifier identifier = gunIdentifiers.get(position);

        // Display unique identifier
        holder.textIdentifier.setText(identifier.getUniqueIdentifier());

        // Set up manufacturing date
        holder.editManufactureDate.setText(identifier.getManufacturingDate());
        holder.editManufactureDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                identifier.setManufacturingDate(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set up manufacturing place
        holder.editManufacturePlace.setText(identifier.getManufacturingPlace());
        holder.editManufacturePlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                identifier.setManufacturingPlace(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public int getItemCount() {
        return gunIdentifiers.size();
    }

    public static class IdentifierViewHolder extends RecyclerView.ViewHolder {

        TextView textIdentifier;
        EditText editManufactureDate, editManufacturePlace;

        public IdentifierViewHolder(@NonNull View itemView) {
            super(itemView);
            textIdentifier = itemView.findViewById(R.id.textIdentifier);
            editManufactureDate = itemView.findViewById(R.id.editManufactureDate);
            editManufacturePlace = itemView.findViewById(R.id.editManufacturePlace);
        }
    }
}

