package com.example.familyapp.view.main.member;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.familyapp.R;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.view.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

public class ChangeRoleDialog extends BaseDialog implements AdapterView.OnItemSelectedListener {

    private OnChangeRoleListener listener;
    private String mOriginalRole;

    public ChangeRoleDialog(@NonNull Context context, String mOriginalRole, OnChangeRoleListener listener) {
        super(context);
        this.listener = listener;
        this.mOriginalRole = mOriginalRole;
    }

    private AppCompatTextView tvMemberRole;
    private List<String> mRoles;

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_change_role;
    }

    @Override
    protected void initView() {
// Spinner element
        final Spinner spinner = findViewById(R.id.spinner_member_role);
        tvMemberRole = findViewById(R.id.tv_member_role);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        mRoles = new ArrayList<String>();
        mRoles.add(getContext().getString(R.string.tv_role_member));
        mRoles.add(getContext().getString(R.string.tv_role_admin));

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mRoles);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        for (int i = 0; i < mRoles.size(); i ++) {
            if (mRoles.get(i).equals(mOriginalRole)) {
                spinner.setSelection(i);
                break;
            }
        }

        AppCompatButton btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChangedRole(tvMemberRole.getText().toString().trim(), spinner.getSelectedItemPosition());
                dismiss();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvMemberRole.setText(mRoles.get(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnChangeRoleListener {
        void onChangedRole(String roleName, int role);
    }
}
