package com.example.familyapp.view.main.group;

import android.os.Bundle;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.model.Group;
import com.example.familyapp.view.base.BaseDialogFragment;
import com.example.familyapp.viewmodel.group.GroupViewModel;
import com.example.familyapp.viewmodel.group.IGroupViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;

public class CreateGroupDialog extends BaseDialogFragment<IGroupViewModel> {

    private String groupID;
    private String groupName;
    private OnCreateGroupListener listener;

    public CreateGroupDialog(OnCreateGroupListener listener) {
        this.listener = listener;
    }

    public CreateGroupDialog(String groupID, String groupName, OnCreateGroupListener listener) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.listener = listener;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_create_group;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppCompatButton createBtn = view.findViewById(R.id.btn_create);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        final AppCompatEditText groupNameEdt = view.findViewById(R.id.edt_group_name);

        tvTitle.setText(getString(groupName != null ? R.string.title_update_group : R.string.title_create_group));
        createBtn.setText(getString(groupName != null ? R.string.btn_update : R.string.btn_create));
        groupNameEdt.setText(groupName);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = groupNameEdt.getText().toString().trim();
                if (!text.isEmpty()) {
                    mViewModel.createGroup(groupID, text);
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
        mViewModel.myRequestGroup().observe(getViewLifecycleOwner(), new Observer<Group>() {
            @Override
            public void onChanged(Group group) {
                listener.onCreatedGroup(group);
                dismiss();
            }
        });
    }

    interface OnCreateGroupListener {
        void onCreatedGroup(Group group);
    }
}
