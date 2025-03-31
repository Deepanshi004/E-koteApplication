package com.example.e_koteapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MaintenanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private final List<GunMaintenance> maintenanceList;

    public MaintenanceAdapter(List<GunMaintenance> maintenanceList) {
        this.maintenanceList = maintenanceList;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_maintenance, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.simple_list_1, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            GunMaintenance maintenance = maintenanceList.get(position - 1); // -1 for header
            ItemViewHolder itemHolder = (ItemViewHolder) holder;

            itemHolder.gunIdTextView.setText(maintenance.getGunId());
            itemHolder.maintenanceDateTextView.setText(maintenance.getMaintenanceDate());

            if (maintenance.getGunName() != null && maintenance.getGunModel() != null) {
                itemHolder.gunNameTextView.setText(maintenance.getGunName());
                itemHolder.gunModelTextView.setText(maintenance.getGunModel());
            }
        }
    }

    @Override
    public int getItemCount() {
        return maintenanceList.size() + 1; // +1 for the header
    }

    // Header ViewHolder
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    // Item ViewHolder
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView gunIdTextView, gunNameTextView, gunModelTextView, maintenanceDateTextView;

        ItemViewHolder(View itemView) {
            super(itemView);
            gunIdTextView = itemView.findViewById(R.id.textGunId);
            gunNameTextView = itemView.findViewById(R.id.textGunName);
            gunModelTextView = itemView.findViewById(R.id.textGunModel);
            maintenanceDateTextView = itemView.findViewById(R.id.textMaintenanceDate);
        }
    }
}
