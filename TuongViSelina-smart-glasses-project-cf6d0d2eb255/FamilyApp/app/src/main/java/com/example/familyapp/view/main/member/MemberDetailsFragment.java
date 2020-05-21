package com.example.familyapp.view.main.member;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Member;
import com.example.familyapp.model.Profile;
import com.example.familyapp.view.base.BaseDialogFragment;
import com.example.familyapp.view.main.group.GroupDetailsActivity;
import com.example.familyapp.viewmodel.group.GroupViewModel;
import com.example.familyapp.viewmodel.group.IGroupViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class MemberDetailsFragment extends BaseDialogFragment<IGroupViewModel> implements View.OnClickListener {

    private AppCompatTextView btnRemoveMember, btnEditMemberRole;
    private AppCompatTextView tvMemberName, tvMemberPhone, tvMemberRole;
    private AppCompatImageView imgAvatar;
    private String groupID;
    private Member mMember;
    private boolean isFromAdmin;

    public MemberDetailsFragment(String groupID, Member mMember, boolean isFromAdmin) {
        this.groupID = groupID;
        this.mMember = mMember;
        this.isFromAdmin = isFromAdmin;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_member_details;
    }

    @Override
    protected IGroupViewModel getViewModel() {
        return new GroupViewModel(getContext());
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvMemberName = view.findViewById(R.id.tv_member_name);
        tvMemberPhone = view.findViewById(R.id.tv_member_phone);
        tvMemberRole = view.findViewById(R.id.tv_member_role);
        btnRemoveMember = view.findViewById(R.id.btn_remove);
        btnEditMemberRole = view.findViewById(R.id.btn_change_role);
        imgAvatar = view.findViewById(R.id.img_member_avatar);

        if (mMember != null) {
            tvMemberName.setText(mMember.getName());
            tvMemberPhone.setText(mMember.getPhone());
            switch (mMember.getRole()) {
                case Member.ADMIN: {
                    tvMemberRole.setText(getString(R.string.tv_role_admin));
                    break;
                }
                case Member.MEMBER: {
                    tvMemberRole.setText(getString(R.string.tv_role_member));
                    break;
                }
                case Member.IVITING_MEMBER: {
                    tvMemberRole.setText(getString(R.string.tv_role_inviting));
                    break;
                }
            }

//            Picasso.with(getContext())
//                    .load(mMember.getAvatar())
//                    .memoryPolicy(MemoryPolicy.NO_STORE)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .into(imgAvatar);

            Profile profile = Prefs.getInstance(getContext()).getProfile();
            if (isFromAdmin) {
                btnEditMemberRole.setVisibility(View.GONE);
                if (profile.getId().equals(mMember.getId())) {
                    btnRemoveMember.setVisibility(View.GONE);
                } else {
                    btnRemoveMember.setText(getString(R.string.btn_remove));
                    btnRemoveMember.setVisibility(View.VISIBLE);
                    if (mMember.getRole() == Member.MEMBER) {
                        btnEditMemberRole.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                if (profile.getId().equals(mMember.getId())) {
                    btnRemoveMember.setText(getString(R.string.btn_leave_group));
                    btnRemoveMember.setVisibility(View.VISIBLE);
                } else {
                    btnRemoveMember.setVisibility(View.GONE);
                }
                btnEditMemberRole.setVisibility(View.GONE);
            }
        }

        btnEditMemberRole.setOnClickListener(this);
        btnRemoveMember.setOnClickListener(this);
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_change_role) {
            new ChangeRoleDialog(getContext(), tvMemberRole.getText().toString().trim(), new ChangeRoleDialog.OnChangeRoleListener() {
                @Override
                public void onChangedRole(String roleName, final int role) {
                    mViewModel.changeMemberRole(groupID, mMember.getId(), role, new OnRequestSuccess<Integer>() {
                        @Override
                        public void onSuccess(Integer data) {
                            mMember.setRole(data);
                            if (mMember.getRole() == Member.ADMIN) {
                                tvMemberRole.setText(getString(R.string.tv_role_admin));
                            } else {
                                tvMemberRole.setText(getString(R.string.tv_role_member));
                            }
                            ((GroupDetailsActivity) Objects.requireNonNull(getActivity())).onChangeMemberRole(mMember.getId(), role);
                        }
                    });
                }
            }).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getString(R.string.msg_remove_member));
            builder.setCancelable(false);
            builder.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    mViewModel.removeMember(groupID, mMember.getId(), new OnRequestSuccess<Boolean>() {
                        @Override
                        public void onSuccess(Boolean data) {
                            MemberDetailsFragment.this.dismiss();
                            ((GroupDetailsActivity) Objects.requireNonNull(getActivity())).onRemoveMember(mMember.getId());
                        }
                    });
                }
            }); builder.setNegativeButton(getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog= builder.create();
            alertDialog.show();
        }
    }
}
