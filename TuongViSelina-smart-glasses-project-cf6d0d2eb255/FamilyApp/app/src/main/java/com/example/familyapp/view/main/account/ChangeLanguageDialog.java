package com.example.familyapp.view.main.account;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.familyapp.R;
import com.example.familyapp.data.Constant;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Language;
import com.example.familyapp.utils.Utils;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.view.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

public class ChangeLanguageDialog extends BaseDialog implements AdapterView.OnItemSelectedListener {

    private OnChangeLanguageListener listener;
    public ChangeLanguageDialog(@NonNull Context context, OnChangeLanguageListener listener) {
        super(context);
        this.listener = listener;
    }

    private AppCompatTextView tvLanguage;
    private List<String> mString;
    private List<Language> mLanguages;
    private Language selectedLanguage;

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_change_language;
    }

    @Override
    protected void initView() {
        // Spinner element
        Spinner spinner = findViewById(R.id.spinner_language);
        tvLanguage = findViewById(R.id.tv_language);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        mString = new ArrayList<String>();
        mString.add(getContext().getString(R.string.tv_en));
        mString.add(getContext().getString(R.string.tv_vi));

        mLanguages = new ArrayList<>();
        mLanguages.add(new Language(getContext().getString(R.string.tv_en), Constant.LANGUAGE_EN));
        mLanguages.add(new Language(getContext().getString(R.string.tv_vi), Constant.LANGUAGE_VI));

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mString);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        if (Constant.LANGUAGE_EN.equals(Prefs.getInstance(getContext()).getLanguage())) {
            spinner.setSelection(mString.indexOf(getContext().getString(R.string.tv_en)));
            selectedLanguage = mLanguages.get(0);
        } else {
            spinner.setSelection(mString.indexOf(getContext().getString(R.string.tv_vi)));
            selectedLanguage = mLanguages.get(1);
        }

        AppCompatButton btnSave = findViewById(R.id.btn_save);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Prefs.getInstance(getContext()).getLanguage().equals(selectedLanguage.getId())) {
                    Prefs.getInstance(getContext()).saveLanguage(selectedLanguage.getId());
                    listener.onLanguageChanged();
                }
                dismiss();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tvLanguage.setText(mLanguages.get(position).getName());
        selectedLanguage = mLanguages.get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface OnChangeLanguageListener {
        void onLanguageChanged();
    }
}
