package com.example.familyapp.view.main.glasses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.example.familyapp.R;
import com.example.familyapp.model.Blind;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.utils.Utils;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.view.main.group.GroupDetailsActivity;
import com.example.familyapp.view.main.sos.DownloadImageGPSAsyncTask;
import com.example.familyapp.view.main.sos.GPSImage;
import com.example.familyapp.viewmodel.glasses.GlassesViewModel;
import com.example.familyapp.viewmodel.glasses.IGlassesViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class GlassesDetailsActivity extends BaseActivity<IGlassesViewModel> {

    public static final String GLASSES_DATA = "glasses_data";

    private AppCompatTextView tvGlassesName, tvGlassesAddress, tvBlinderName, tvBlindPhone, tvGlassesID;
    private CircleImageView imgGlasses;
    private GlassesImageAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_glasses_details;
    }

    @Override
    protected IGlassesViewModel getViewModel() {
        return new GlassesViewModel(this);
    }

    @Override
    protected void initView() {
        imgGlasses = findViewById(R.id.img_glasses);
        tvGlassesName = findViewById(R.id.tv_glasses_name);
        tvGlassesID = findViewById(R.id.tv_glasses_id);
        tvGlassesAddress = findViewById(R.id.tv_glasses_address);
        tvBlinderName = findViewById(R.id.tv_blinder_name);
        tvBlindPhone = findViewById(R.id.tv_blinder_age);
        View layoutAction = findViewById(R.id.layout_action);
        AppCompatButton btnRemoveGlasses = findViewById(R.id.btn_remove_glasses);
        AppCompatButton btnUpdateGlasses = findViewById(R.id.btn_update_glasses);
        final View btnGallery = findViewById(R.id.btn_gallery);
        final View icExpandable = findViewById(R.id.ic_expandable);
        final View breakLine = findViewById(R.id.break_line_1);

        btnRemoveGlasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GlassesDetailsActivity.this);
                builder.setMessage(getString(R.string.msg_delete_glass));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mViewModel.removeGlasses(Objects.requireNonNull(mViewModel.myRequestGlasses().getValue()).getLinkID());
                    }
                });
                builder.setNegativeButton(getString(R.string.btn_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnUpdateGlasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateGlassesDialog(mViewModel.myRequestGlasses().getValue(), new UpdateGlassesDialog.UpdateGlassesListener() {
                    @Override
                    public void onUpdated(Glasses glasses) {
                        mViewModel.setGlasses(glasses);
                    }
                }).show(getSupportFragmentManager(), null);
            }
        });

        boolean isFromAdmin = getIntent().getBooleanExtra(GroupDetailsActivity.IS_FROM_ADMIN, false);
        layoutAction.setVisibility(isFromAdmin ? View.VISIBLE : View.GONE);
        breakLine.setVisibility(isFromAdmin ? View.VISIBLE : View.GONE);

        final RecyclerView recyclerView = findViewById(R.id.img_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new GlassesImageAdapter(this, new ArrayList<String>());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    icExpandable.setRotation(-90);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    icExpandable.setRotation(0);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setGlassesData() {
        Glasses mGlasses = mViewModel.myRequestGlasses().getValue();
        if (mGlasses != null) {
            tvGlassesName.setText(mGlasses.getGlassesName());
            tvGlassesID.setText(String.format(getString(R.string.tv_glass_id_), mGlasses.getGlassesId()));
            tvBlinderName.setText(formatValue(getString(R.string.tv_blind_name), mGlasses.getBlind().getName()));
            tvBlindPhone.setText(formatValue(getString(R.string.hint_blind_age), mGlasses.getBlind().getAge()));
            tvGlassesAddress.setText(formatValue(getString(R.string.tv_blind_address), mGlasses.getBlind().getAddress()));
        }
    }

    private SpannableStringBuilder formatValue(String value1, String value2) {
        SpannableStringBuilder str = new SpannableStringBuilder(value1 + ": " + value2);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,value1.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return str;
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.myRequestGlasses().observe(this, new Observer<Glasses>() {
            @Override
            public void onChanged(Glasses glasses) {
                if (glasses == null) {
                    onBackPressed();
                    return;
                }
                setGlassesData();
            }
        });

        Glasses glasses = (Glasses) getIntent().getSerializableExtra(GLASSES_DATA);
        if (glasses != null) {
            mViewModel.setGlasses(glasses);
            mViewModel.getSOSImages(glasses.getGlassesId(), new OnRequestSuccess<ArrayList<String>>() {
                @Override
                public void onSuccess(final ArrayList<String> list) {
                    Collections.reverse(list);
                    showLoading(true);
                    new DownloadImageGPSAsyncTask(GlassesDetailsActivity.this, list.get(0), new DownloadImageGPSAsyncTask.DownloadImageListener() {
                        @Override
                        public void onSuccess(GPSImage data) {
                            if (data != null) {
                                imgGlasses.setImageBitmap(data.getBitmap());
                            }
                            adapter.setList(list);
                            showLoading(false);
                        }
                    }).execute();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
