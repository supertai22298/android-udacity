package com.example.familyapp.view.tutorial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.familyapp.R;
import com.google.android.gms.maps.model.Dot;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class DotAdapter extends RecyclerView.Adapter<DotAdapter.DotViewHolder> {

    private int currentFocus = 0;

    public void setCurrentFocus(int currentFocus) {
        this.currentFocus = currentFocus;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DotViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DotViewHolder holder, int position) {
        if (position == currentFocus) {
            holder.imgDot.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_gradient));
        } else {
            holder.imgDot.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.btn_disable));
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class DotViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imgDot;
        public DotViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDot = itemView.findViewById(R.id.img_dot);
        }
    }

}
