package com.example.familyapp.view.main.glasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.familyapp.R;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.view.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class GlassesImageAdapter extends RecyclerView.Adapter<GlassesImageAdapter.GlassesImageViewHolder> {

    private Context context;
    private ArrayList<String> list;

    public GlassesImageAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GlassesImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GlassesImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_glasses_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GlassesImageViewHolder holder, final int position) {
        Picasso.with(holder.imgGlasses.getContext())
                .load(list.get(position))
                .resize(300, 300)
                .placeholder(R.color.colorAccent)
                .into(holder.imgGlasses);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class GlassesImageViewHolder extends RecyclerView.ViewHolder {
        SquareImageView imgGlasses;

        GlassesImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGlasses = itemView.findViewById(R.id.img_glasses);
        }
    }
}
