package com.example.familyapp.view.main.group;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.familyapp.R;
import com.example.familyapp.model.Group;
import com.example.familyapp.view.base.BaseDialogFragment;
import com.example.familyapp.viewmodel.group.GroupViewModel;
import com.example.familyapp.viewmodel.group.IGroupViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.Observer;

public class InviteMemberDialog extends BaseDialogFragment<IGroupViewModel> {

    private String groupID;
    private String groupName;
    private OnRequestSuccess<String> listener;

    public InviteMemberDialog(String groupID, String groupName, OnRequestSuccess<String> listener) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.listener = listener;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_invite_member;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppCompatButton btnInvite = view.findViewById(R.id.btn_invite_member);
        final AppCompatEditText edtUserName = view.findViewById(R.id.edt_user_name);

        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtUserName.getText().toString().trim();
                if (!userName.isEmpty()) {
                    mViewModel.inviteMember(groupID, userName, groupName);
                }
            }
        });
    }

    @Override
    protected IGroupViewModel getViewModel() {
        return new GroupViewModel(getContext());
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.isRequestSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSuccess) {
                Toast.makeText(getContext(), Objects.requireNonNull(getContext()).getString(R.string.msg_invitation_success), Toast.LENGTH_LONG).show();
                dismiss();
                listener.onSuccess(groupID);
            }
        });
    }
}
