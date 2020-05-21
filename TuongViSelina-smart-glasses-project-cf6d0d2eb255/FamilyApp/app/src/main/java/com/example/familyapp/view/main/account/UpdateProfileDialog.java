package com.example.familyapp.view.main.account;

import android.os.Bundle;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Group;
import com.example.familyapp.model.Profile;
import com.example.familyapp.view.base.BaseDialogFragment;
import com.example.familyapp.viewmodel.base.IViewModel;
import com.example.familyapp.viewmodel.group.GroupViewModel;
import com.example.familyapp.viewmodel.group.IGroupViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;
import com.example.familyapp.viewmodel.profile.IProfileViewModel;
import com.example.familyapp.viewmodel.profile.ProfileViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;

public class UpdateProfileDialog extends BaseDialogFragment<IViewModel> {

    private UpdateProfileListener listener;

    public UpdateProfileDialog(UpdateProfileListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_update_user_info;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppCompatTextView updateBtn = view.findViewById(R.id.btn_update);
        final AppCompatEditText edtFullName = view.findViewById(R.id.edt_user_name);
        final AppCompatEditText edtPhone = view.findViewById(R.id.edt_phone);

        final Profile profile = Prefs.getInstance(getContext()).getProfile();
        if (profile != null) {
            edtFullName.setText(profile.getName());
            edtPhone.setText(profile.getPhone());
        }

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtFullName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                if (!name.isEmpty() && !phone.isEmpty()) {
                    if (profile != null && (!name.equals(profile.getName()) || !phone.equals(profile.getPhone()))) {
                        listener.onUpdated(name, phone);
                    }
                    dismiss();
                }
            }
        });
    }

    @Override
    protected IViewModel getViewModel() {
        return null;
    }

    public interface UpdateProfileListener {
        void onUpdated(String name, String phone);
    }
}
