package com.example.familyapp.view.main.glasses;

import android.os.Bundle;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.view.base.BaseDialogFragment;
import com.example.familyapp.viewmodel.glasses.GlassesViewModel;
import com.example.familyapp.viewmodel.glasses.IGlassesViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;

public class UpdateGlassesDialog extends BaseDialogFragment<IGlassesViewModel> {

    private Glasses glasses;
    private UpdateGlassesListener listener;

    public UpdateGlassesDialog(Glasses glasses, UpdateGlassesListener listener) {
        this.glasses = glasses;
        this.listener = listener;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_update_glasses_info;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        AppCompatTextView updateBtn = view.findViewById(R.id.btn_update);
        final AppCompatEditText edtGlassesName = view.findViewById(R.id.edt_glass_name);
        final AppCompatEditText edtBlindName = view.findViewById(R.id.edt_blind_name);
        final AppCompatEditText edtBlindAddress = view.findViewById(R.id.edt_blind_address);
        final AppCompatEditText edtBlindAge = view.findViewById(R.id.edt_blind_age);

            edtGlassesName.setText(glasses.getGlassesName());
            edtBlindName.setText(glasses.getBlind().getName());
            edtBlindAddress.setText(glasses.getBlind().getAddress());
            edtBlindAge.setText(glasses.getBlind().getAge());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String glassesName = edtGlassesName.getText().toString().trim();
                String blindName = edtBlindName.getText().toString().trim();
                String blindAddress = edtBlindAddress.getText().toString().trim();
                String blindAge = edtBlindAge.getText().toString().trim();
                Glasses newGlasses = new Glasses(glassesName, blindName, blindAddress, blindAge);
                if (!glasses.toString().equals(newGlasses.toString())) {
                    glasses.setGlassesName(glassesName);
                    glasses.setBlind(newGlasses.getBlind());
                    mViewModel.updateGlasses(glasses);
                    return;
                }
                dismiss();
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
        mViewModel.myRequestGlasses().observe(getViewLifecycleOwner(), new Observer<Glasses>() {
            @Override
            public void onChanged(Glasses glasses) {
                listener.onUpdated(glasses);
                dismiss();
            }
        });
    }

    public interface UpdateGlassesListener {
        void onUpdated(Glasses glasses);
    }
}
