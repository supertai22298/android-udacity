package com.example.familyapp.view.main.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.familyapp.R;
import com.example.familyapp.model.Glasses;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class HomeGlassesAdapter extends RecyclerView.Adapter<HomeGlassesAdapter.GlassesViewHolder> {

    private ArrayList<Glasses> glasses;
    private GlassesSelectListener listener;

    public HomeGlassesAdapter(ArrayList<Glasses> glasses, GlassesSelectListener listener) {
        this.glasses = glasses;
        this.listener = listener;
    }

    public void setGlasses(ArrayList<Glasses> glasses) {
        this.glasses = glasses;
        if (!glasses.isEmpty()) {
            glasses.get(0).setSelected(true);
        }
        notifyDataSetChanged();
    }

    public Glasses getGlasses(int index) {
        if (index != - 1 && index < glasses.size()) {
            return glasses.get(index);
        }
        return null;
    }

    public Glasses getSelected() {
        for (int i = 0; i < glasses.size(); i ++) {
            if (glasses.get(i).isSelected()) {
                return glasses.get(i);
            }
        }
        return null;
    }

    @NonNull
    @Override
    public GlassesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GlassesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_glasses, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GlassesViewHolder holder, final int position) {
        final Glasses data = glasses.get(position);
        holder.itemView.setAlpha(data.isSelected() ? 1.0f : 0.3f);
            holder.tvGlassesAddress.setText(data.getGps().getAddress());
            holder.tvBlindName.setText(data.getBlind().getName());
            holder.imgLocation.setImageBitmap(data.getGps().getBitmap());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.isSelected()) {
                    resetSelection(position);
                    listener.onSelected(data, position);
                }
            }
        });
    }

    private void resetSelection(int position) {
        for (int i = 0; i < glasses.size(); i ++) {
            glasses.get(i).setSelected(i == position);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return glasses.size();
    }

    static class GlassesViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvGlassesAddress, tvBlindName;
        AppCompatImageView imgLocation;
        GlassesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGlassesAddress = itemView.findViewById(R.id.tv_blinder_address);
            tvBlindName = itemView.findViewById(R.id.tv_blinder_name);
            imgLocation = itemView.findViewById(R.id.img_location);
        }
    }

    public interface GlassesSelectListener {
        void onSelected(Glasses glasses, int position);
    }

}
