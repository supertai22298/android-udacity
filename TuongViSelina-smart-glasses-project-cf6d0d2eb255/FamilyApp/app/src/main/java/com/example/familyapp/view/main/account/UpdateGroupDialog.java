package com.example.familyapp.view.main.account;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Profile;
import com.example.familyapp.view.base.BaseDialog;
import com.example.familyapp.view.base.BaseDialogFragment;
import com.example.familyapp.viewmodel.base.IViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

public class UpdateGroupDialog extends BaseDialog {

    private String groupName;
    private OnRequestSuccess<String> listener;

    public UpdateGroupDialog(Context context, String groupName, OnRequestSuccess<String> listener) {
        super(context);
        this.groupName = groupName;
        this.listener = listener;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_update_group;
    }

    @Override
    protected void initView() {
        AppCompatTextView updateBtn = findViewById(R.id.btn_update);
        final AppCompatEditText edtGroupName = findViewById(R.id.edt_group_name);
        edtGroupName.setText(groupName);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtGroupName.getText().toString().trim();
                if (!name.isEmpty() && !name.equals(groupName)) {
                    listener.onSuccess(name);
                }
                dismiss();
            }
        });
    }
}
