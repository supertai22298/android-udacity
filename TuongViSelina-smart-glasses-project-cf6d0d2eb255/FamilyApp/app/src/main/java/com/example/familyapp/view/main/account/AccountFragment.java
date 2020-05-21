package com.example.familyapp.view.main.account;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.application.App;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Profile;
import com.example.familyapp.utils.Utils;
import com.example.familyapp.view.base.BaseFragment;
import com.example.familyapp.view.main.MainActivity;
import com.example.familyapp.view.main.SplashActivity;
import com.example.familyapp.view.profile.ProfileActivity;
import com.example.familyapp.view.signin.SignInActivity;
import com.example.familyapp.viewmodel.base.IViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;
import com.example.familyapp.viewmodel.profile.IProfileViewModel;
import com.example.familyapp.viewmodel.profile.ProfileViewModel;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;

public class AccountFragment extends BaseFragment<IProfileViewModel> {
    private static final int SELECT_PHOTO_GALLERY = 111;
    private static final int TAKE_A_PHOTO = 222;

    private AppCompatImageView imgAvatar;
    private AppCompatTextView tvUserPhone, tvUsername;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_account;
    }

    @Override
    protected IProfileViewModel getViewModel() {
        return new ProfileViewModel(getContext());
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final AppCompatTextView tvEditProfile = view.findViewById(R.id.tv_edit_profile);
        final AppCompatButton tvSignOut = view.findViewById(R.id.tv_sign_out);
        final AppCompatTextView tvChangeLanguage = view.findViewById(R.id.btn_change_language);
        tvUsername = view.findViewById(R.id.tv_user_name);
        tvUserPhone = view.findViewById(R.id.tv_user_phone);
        imgAvatar = view.findViewById(R.id.img_user_avatar);

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.checkPermission(new OnRequestSuccess<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        if (data) {
                            new UpdateAvatarDialog(new UpdateAvatarDialog.OnUploadAvatarListener() {
                                @Override
                                public void onGallerySelected() {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO_GALLERY);
                                }

                                @Override
                                public void onCameraSelected() {
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    getActivity().startActivityForResult(takePictureIntent, TAKE_A_PHOTO);
                                }
                            }).show(getChildFragmentManager(), null);
                        }
                    }
                });
            }
        });

        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UpdateProfileDialog(new UpdateProfileDialog.UpdateProfileListener() {
                    @Override
                    public void onUpdated(String name, String phone) {
                        mViewModel.updateProfile(name, phone, false, null);
                    }
                }).show(getChildFragmentManager(), null);
            }
        });
        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(getString(R.string.msg_logout));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        mViewModel.logout(new OnRequestSuccess<Boolean>() {
                            @Override
                            public void onSuccess(Boolean data) {
                                String lang = Prefs.getInstance(getContext()).getLanguage();
                                Prefs.getInstance(getContext()).clear();
                                Prefs.getInstance(getContext()).saveLanguage(lang);
                                startActivity(new Intent(getContext(), SignInActivity.class));
                                getActivity().finish();
                            }
                        });
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

        tvChangeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChangeLanguageDialog(getContext(), new ChangeLanguageDialog.OnChangeLanguageListener() {
                    @Override
                    public void onLanguageChanged() {
                        Utils.changeLanguage(getActivity());
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                }).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        showProfile();
    }

    private void showProfile() {
        Profile profile = Prefs.getInstance(getContext()).getProfile();
        if (profile != null) {
            tvUsername.setText(profile.getName());
            tvUserPhone.setText(profile.getPhone());
            Picasso.with(getContext())
                    .load(profile.getAvatar())
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imgAvatar);
        }
    }

    private void setAvatar(Uri uri) {
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        ContentResolver cr = getContext().getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
            imgAvatar.setImageBitmap(bitmap);
            updateAvatar(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.setRxPermissions(new RxPermissions(this));
        mViewModel.isRequestSuccess().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                showProfile();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case SELECT_PHOTO_GALLERY:
                    setAvatar(data.getData());
                    break;
                case TAKE_A_PHOTO:
                    Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    if (bitmap != null) {
                        imgAvatar.setImageBitmap(bitmap);
                        updateAvatar(bitmap);
                    }
                    break;
            }
        }
    }

    private void updateAvatar(Bitmap bitmap) {
        Profile profile = Prefs.getInstance(getContext()).getProfile();
        mViewModel.updateProfile(profile.getName(), profile.getPhone(), true, Utils.convertBitmap(bitmap));
    }
}