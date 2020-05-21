package com.example.familyapp.view.main.member;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.familyapp.R;
import com.example.familyapp.model.Member;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ExampleViewHolder> {

    public static final int GROUP_MEMBER = 0;
    public static final int INVITE_MEMBER = 1;
    public static final int REQUEST_JOIN_GROUP = 2;

    private Context mContext;
    private ArrayList<Member> list;
    private int type;
    private OnMemberClickListener listener;
    public MemberAdapter(Context context, ArrayList<Member> list, int type, OnMemberClickListener listener){
        mContext=context;
        this.list =list;
        this.type = type;
        this.listener = listener;
    }

    public void setList(ArrayList<Member> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.item_member, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        final Member currentItem = list.get(position);
        holder.tvMemberName.setText(currentItem.getName());
        holder.tvMemberPhone.setText(currentItem.getPhone());
        switch (currentItem.getRole()) {
            case Member.ADMIN: {
                holder.tvMemberRole.setText(mContext.getString(R.string.tv_role_admin));
                break;
            }
            case Member.MEMBER: {
                holder.tvMemberRole.setText(mContext.getString(R.string.tv_role_member));
                break;
            }
            case Member.IVITING_MEMBER: {
                holder.tvMemberRole.setText(mContext.getString(R.string.tv_role_inviting));
                break;
            }
        }

        switch (type) {
            case INVITE_MEMBER:
                holder.btnInviteMember.setVisibility(View.VISIBLE);
                holder.tvMemberRole.setVisibility(View.GONE);
                break;
            case REQUEST_JOIN_GROUP:
                holder.btnInviteMember.setVisibility(View.GONE);
                holder.tvMemberRole.setVisibility(View.GONE);
                break;
            default:
                holder.btnInviteMember.setVisibility(View.GONE);
                holder.tvMemberRole.setVisibility(View.VISIBLE);
                break;
        }



        Picasso.with(mContext)
                .load(currentItem.getAvatar())
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.imgAvatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMemberClicked(currentItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder{
        public TextView tvMemberName, tvMemberPhone, tvMemberRole;
        public AppCompatButton btnInviteMember;
        AppCompatImageView imgAvatar;
        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tv_member_name);
            tvMemberPhone = itemView.findViewById(R.id.tv_member_phone);
            tvMemberRole = itemView.findViewById(R.id.tv_member_role);
            btnInviteMember = itemView.findViewById(R.id.btn_invite_member);
            imgAvatar = itemView.findViewById(R.id.img_member_avatar);
        }
    }

    public interface OnMemberClickListener {
        void onMemberClicked(Member member);
    }
}
