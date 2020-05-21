package com.example.familyapp.view.main.glasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.familyapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

public class VerifyCodesAdapter extends RecyclerView.Adapter<VerifyCodesAdapter.CodeViewHolder>  {

    private ArrayList<String> codes;

    public VerifyCodesAdapter(ArrayList<String> codes) {
        this.codes = codes;
    }

    public void setCodes(ArrayList<String> codes) {
        this.codes = codes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CodeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_verify, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {
        if (codes.size() > position) {
            holder.tvCode.setText(codes.get(position));
        } else {
            holder.tvCode.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class CodeViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tvCode;
        public CodeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tv_code);
        }
    }
}
