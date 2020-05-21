package com.example.familyapp.view.main.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyapp.R;
import com.example.familyapp.model.Group;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    public static final int MY_GROUP = 0;
    private static final int INVITATIONS = 1;
    static final int SEARCH_GROUP = 2;

    private Context context;
    private ArrayList<Group> groupList;
    private int type;
    private OnGroupClickListener listener;

    GroupAdapter(Context context, ArrayList<Group> groupList, int type, OnGroupClickListener listener) {
        this.context = context;
        this.groupList = groupList;
        this.type = type;
        this.listener = listener;
    }

    void addGroup(Group group) {
     groupList.add(group);
        notifyDataSetChanged();
    }

    void setGroupList(ArrayList<Group> groupList) {
        this.groupList.clear();
        this.groupList.addAll(groupList);
        notifyDataSetChanged();
    }

    public ArrayList<Group> getGroups() {
        return groupList;
   }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        final Group group = groupList.get(position);
        holder.tvGroupName.setText(group.getName());
        holder.tvGroupCharacter.setText(group.getFirstCharacter());
        holder.tvCountMember.setText(group.getCountMemberText(context));

        switch (type) {
            case SEARCH_GROUP:
                holder.btnAccept.setVisibility(View.VISIBLE);
                holder.btnReject.setVisibility(View.GONE);
                holder.btnAccept.setText(context.getText(R.string.btn_join_group));
                break;
            case INVITATIONS:
                holder.btnReject.setVisibility(View.VISIBLE);
                holder.btnAccept.setVisibility(View.VISIBLE);
                holder.btnAccept.setText(context.getText(R.string.btn_accept));
                break;
            default:
                holder.btnReject.setVisibility(View.GONE);
                holder.btnAccept.setVisibility(View.GONE);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == MY_GROUP) {
                    listener.onGroupClicked(group);
                }
            }
        });

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onActionClicked(group, type, true);
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onActionClicked(group, type, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder{

        TextView tvGroupName, tvGroupCharacter, tvCountMember;
        AppCompatButton btnAccept, btnReject;

        GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tv_group_name);
            tvGroupCharacter = itemView.findViewById(R.id.tv_group_character);
            tvCountMember = itemView.findViewById(R.id.tv_group_count_member);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnReject = itemView.findViewById(R.id.btn_reject);
        }
    }

    interface OnGroupClickListener {
        void onGroupClicked(Group group);
        void onActionClicked(Group group, int type, boolean isAccepted);
    }
}
