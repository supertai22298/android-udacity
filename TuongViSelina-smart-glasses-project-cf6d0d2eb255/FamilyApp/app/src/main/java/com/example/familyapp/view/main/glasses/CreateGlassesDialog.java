package com.example.familyapp.view.main.glasses;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import com.example.familyapp.R;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.view.base.BaseDialogFragment;
import com.example.familyapp.viewmodel.glasses.GlassesViewModel;
import com.example.familyapp.viewmodel.glasses.IGlassesViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.Observer;

public class CreateGlassesDialog extends BaseDialogFragment<IGlassesViewModel> {

    private OnCreateGlassesListener listener;
    private String groupID;
    private AppCompatEditText edtGlassName,edtBlindName,edtBlindAddress,edtBlindAge;

    public CreateGlassesDialog(String groupID, OnCreateGlassesListener listener) {
        this.groupID = groupID;
        this.listener = listener;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_create_glasses;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppCompatButton btnCreate = view.findViewById(R.id.btn_create_glasses);
        edtGlassName = view.findViewById(R.id.edt_glass_name);
        final AppCompatEditText edtGlassID = view.findViewById(R.id.edt_glass_id);
        edtBlindName = view.findViewById(R.id.edt_blind_name);
        edtBlindAddress = view.findViewById(R.id.edt_blind_address);
        edtBlindAge = view.findViewById(R.id.edt_blind_age);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String glassID = edtGlassID.getText().toString().trim();
                String blindName = edtBlindName.getText().toString().trim();
                String blindAddress = edtBlindAddress.getText().toString().trim();
                String bindAge = edtBlindAge.getText().toString().trim();
                String glassName = edtGlassName.getText().toString().trim();
                if (!glassID.isEmpty() && !glassName.isEmpty()) {
                    mViewModel.createGlasses(groupID, glassID, glassName, blindName, blindAddress, bindAge);
                }
            }
        });
    }

    @Override
    protected IGlassesViewModel getViewModel() {
        return new GlassesViewModel(getContext());
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.isCreateGlassesSuccess().observe(this, new Observer<String>() {
            @Override
            public void onChanged(final String linkID) {
                new VerifyGlassesDialog(getContext(), new VerifyGlassesDialog.OnVerifyGlassesListener() {
                    @Override
                    public void onSendVerifyCode(String code, final Dialog dialog) {
                        mViewModel.verifyGlasses(linkID, code, new OnRequestSuccess<Boolean>() {
                            @Override
                            public void onSuccess(Boolean data) {
                                dialog.dismiss();
                                CreateGlassesDialog.this.dismiss();
                                listener.onCreatedGlasses();
                            }
                        });
                    }
                }).show();
            }
        });

        mViewModel.myRequestGlasses().observe(this, new Observer<Glasses>() {
            @Override
            public void onChanged(Glasses glasses) {
                listener.onCreatedGlasses();
            }
        });
    }

    public interface OnCreateGlassesListener {
        void onCreatedGlasses();
    }
}
