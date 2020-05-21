package com.example.familyapp.view.main.glasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.familyapp.R;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Group;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class GlassesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Glasses> list;
    private boolean isFromAdmin;
    private OnGlassesClickListener listener;

    public GlassesAdapter(Context context, ArrayList<Glasses> list, boolean isFromAdmin, OnGlassesClickListener listener) {
        this.context = context;
        this.list = list;
        this.isFromAdmin = isFromAdmin;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0 && isFromAdmin) {
            return new AddGlassesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_new_glasses, parent, false));
        } else {
            return new GlassesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_glasses, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setList(ArrayList<Glasses> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    listener.onAddGlassesClicked();
                } else {
                    listener.onGlassesClicked(list.get(position - 1));
                }
            }
        });

        if (position != 0 || !isFromAdmin) {
            int index = position;
            if (isFromAdmin) {
                index = index - 1;
            }
            ((GlassesViewHolder) holder).tvGlassesName.setText("#" + list.get(index).getGlassesId());
        }
    }

    @Override
    public int getItemCount() {
        if (isFromAdmin) {
            return list.size() + 1;
        } else {
            return list.size();
        }
    }

    static class GlassesViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvGlassesName;
        GlassesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGlassesName = itemView.findViewById(R.id.tv_glass_name);
        }
    }

    static class AddGlassesViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvGlassName;
        AddGlassesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGlassName = itemView.findViewById(R.id.tv_glass_name);
        }
    }

    public interface OnGlassesClickListener {
        void onAddGlassesClicked();

        void onGlassesClicked(Glasses glasses);
    }
}
