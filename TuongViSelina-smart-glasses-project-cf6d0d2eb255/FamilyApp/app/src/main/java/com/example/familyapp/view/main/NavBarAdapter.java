package com.example.familyapp.view.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.familyapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class NavBarAdapter extends RecyclerView.Adapter<NavBarAdapter.NavBarViewHolder> {

    private ArrayList<NavBar> navBars;

    public NavBarAdapter(ArrayList<NavBar> navBars) {
        this.navBars = navBars;
    }

    @NonNull
    @Override
    public NavBarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NavBarViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_navigation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NavBarViewHolder holder, int position) {
        NavBar navBar = navBars.get(position);
        holder.icDrawable.setImageDrawable(ContextCompat.getDrawable(holder.icDrawable.getContext(), navBar.getDrawable()));
        holder.icDrawable.setColorFilter(R.color.centerGradient);
        holder.line.setVisibility(navBar.isFocusing() ? View.VISIBLE : View.INVISIBLE);
        holder.topLayout.setVisibility(navBar.isFocusing() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return navBars.size();
    }

    static class NavBarViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView icDrawable;
        View line, topLayout;
        NavBarViewHolder(@NonNull View itemView) {
            super(itemView);
            icDrawable = itemView.findViewById(R.id.ic_drawable);
            line = itemView.findViewById(R.id.line);
            topLayout = itemView.findViewById(R.id.top_layout);
        }
    }
}
