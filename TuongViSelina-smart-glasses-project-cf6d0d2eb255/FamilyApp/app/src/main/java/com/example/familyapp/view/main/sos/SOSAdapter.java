package com.example.familyapp.view.main.sos;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.familyapp.R;
import com.example.familyapp.data.Constant;
import com.example.familyapp.data.notification.Notification;
import com.example.familyapp.data.notification.NotificationDao;
import com.example.familyapp.notification.Event;
import com.example.familyapp.utils.Utils;
import com.example.familyapp.view.main.group.GroupDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.familyapp.notification.OutAppNotificationActivity.DATA;

public class SOSAdapter extends RecyclerView.Adapter<SOSAdapter.NotificationViewHolder> {

    private Context mContext;
    private ArrayList<Notification> list;
    private String type;

    public SOSAdapter(Context context, ArrayList<Notification> list, String type) {
        mContext = context;
        this.list = list;
        this.type = type;
    }

    public void setList(ArrayList<Notification> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_sos, parent, false);
        return new NotificationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationViewHolder holder, final int position) {
        holder.itemView.setAlpha(type != null && position == 0 ? 0.f : 1.f);

        if (list.isEmpty() || (type != null && position == 0)) {
            return;
        }
        final Notification data = list.get(type != null ? position - 1 : position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationDao.getInstance().markAsRead(data.getId());
                data.setRead(true);
                notifyDataSetChanged();
                if (type != null) {
                    mContext.startActivity(new Intent(mContext, SOSDetailsActivity.class)
                            .putExtra(DATA, data.getId()));
                } else {
                    mContext.startActivity(new Intent(mContext, GroupDetailsActivity.class)
                            .putExtra(GroupDetailsActivity.GROUP_ID, data.getReferID()));
                }
            }
        });
        String title, message;
        Resources resources = mContext.getResources();
        if (data.getType().equals(Constant.NOTIFY_SOS)) {
            title = resources.getString(R.string.notify_sos_title);
            message = resources.getString(R.string.notify_sos_message);
            message = String.format(message, data.getReferID());
        } else {
            title = resources.getString(R.string.notify_group_title);
            title = String.format(title, data.getReferID());
            if (data.getType().equals(Constant.NOTIFY_UPDATE_GLASSES)) {
                message = resources.getString(R.string.notify_group_update_glasses_message);
                message = String.format(message, data.getReferValue());
            } else if (data.getType().equals(Constant.NOTIFY_INVITATION)) {
                message = resources.getString(R.string.notify_group_invitation_message);
                message = String.format(message, data.getReferValue());
            } else {
                switch (data.getType()) {
                    case Constant.NOTIFY_ADD_GLASSES:
                        message = resources.getString(R.string.notify_group_add_glasses_message);
                        break;
                    case Constant.NOTIFY_REMOVE_GLASSES:
                        message = resources.getString(R.string.notify_group_remove_glasses_message);
                        break;
                    case Constant.NOTIFY_UPDATE_GROUP:
                        message = resources.getString(R.string.notify_group_update_message);
                        break;
                    case Constant.NOTIFY_QUIT_GROUP:
                        message = resources.getString(R.string.notify_group_quit_message);
                        break;
                    case Constant.NOTIFY_CHANGE_ROLE:
                        message = resources.getString(R.string.notify_group_change_role_message);
                        break;
                    case Constant.NOTIFY_REMOVE_MEMBER:
                        message = resources.getString(R.string.notify_group_remove_member_message);
                        break;
                    default:
                        message = resources.getString(R.string.notify_group_default_message);
                        break;
                }
                message = String.format(message, data.getReferID());
            }
        }
        holder.tvNotificationName.setText(title);
        holder.tvNotificationDescription.setText(message);
        holder.imgNew.setVisibility(!data.isRead() ? View.VISIBLE : View.GONE);
        holder.icNotification.setImageDrawable(ContextCompat.getDrawable(holder.icNotification.getContext(),
                type != null ? R.drawable.ic_sos_notify : R.drawable.ic_normal_notification));
        if (position == (type != null ? 1 : 0)) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_right_to_left);
            holder.itemView.startAnimation(animation);
        }
        holder.tvTimeAgo.setText(Utils.getTimeAgo(data.getTimestamp(), holder.imgNew.getContext()));
    }

    @Override
    public int getItemCount() {
        return type != null ? list.size() + 1 : list.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotificationName, tvNotificationDescription, tvTimeAgo;
        View layoutRoot, imgNew;
        AppCompatImageView icNotification;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationName = itemView.findViewById(R.id.tv_notification_name);
            tvNotificationDescription = itemView.findViewById(R.id.tv_notification_description);
            tvTimeAgo = itemView.findViewById(R.id.tv_time);
            layoutRoot = itemView.findViewById(R.id.layout_root);
            imgNew = itemView.findViewById(R.id.img_new_notification);
            icNotification = itemView.findViewById(R.id.ic_notification);
        }
    }
}
