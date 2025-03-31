package com.example.e_koteapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GunsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Gun> gunList;
    private static final int Header =0;
    private static final int item = 1;

    public GunsAdapter(List<Gun> gunList) {
        this.gunList = gunList;
    }

    public int getItemViewType(int position){
        return position ==0 ? Header : item;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Header){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header,parent,false);
            return new HeaderViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gun, parent, false);
            return new GunViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       if (holder instanceof GunViewHolder) {
            Gun gun = gunList.get(position -1);
            GunViewHolder gunHolder = (GunViewHolder) holder;

            gunHolder.textGunId.setText(gun.getId());
            gunHolder.textGunName.setText(gun.getName());
            gunHolder.textGunModel.setText(gun.getModel());
            gunHolder.textQuantity.setText(String.valueOf(gun.getQuantity()));

            gunHolder.buttonDelete.setOnClickListener(v -> {
                deleteGunFromFirestore(gun.getId(),position-1,gunHolder.itemView.getContext());
            });
        }
    }

    private void deleteGunFromFirestore(String gunid, int position, Context context) {
        FirebaseFirestore.getInstance().collection("guns")
                .document(gunid).delete().addOnSuccessListener(aVoid ->{
                    gunList.remove(position);
                    notifyItemRemoved(position+1);
                    notifyItemRangeChanged(position+1,getItemCount());
                    Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete" +e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return gunList.size() + 1 ;
    }

    public void updateGuns(List<Gun> newGuns) {
        this.gunList.clear();
        this.gunList.addAll(newGuns);
        notifyDataSetChanged();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder{
        public HeaderViewHolder(View itemview){
            super(itemview);

        }
    }

    static class GunViewHolder extends RecyclerView.ViewHolder {
        TextView textGunId, textGunName, textGunModel, textQuantity;
        ImageView buttonDelete;

        GunViewHolder(@NonNull View itemView) {
            super(itemView);
            textGunId = itemView.findViewById(R.id.textGunId);
            textGunName = itemView.findViewById(R.id.textGunName);
            textGunModel = itemView.findViewById(R.id.textGunModel);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
