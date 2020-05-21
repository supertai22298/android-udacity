package com.example.familyapp.view.main.group;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Group;
import com.example.familyapp.model.Profile;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.model.Member;
import com.example.familyapp.view.main.account.UpdateGroupDialog;
import com.example.familyapp.view.main.glasses.CreateGlassesDialog;
import com.example.familyapp.view.main.glasses.GlassesAdapter;
import com.example.familyapp.view.main.glasses.GlassesDetailsActivity;
import com.example.familyapp.view.main.member.MemberAdapter;
import com.example.familyapp.view.main.member.MemberDetailsFragment;
import com.example.familyapp.viewmodel.group.GroupViewModel;
import com.example.familyapp.viewmodel.group.IGroupViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupDetailsActivity extends BaseActivity<IGroupViewModel> implements View.OnClickListener {

    public static final String GROUP_ID = "group_data";
    public static final String IS_FROM_ADMIN = "is_from_admin";

    private MemberAdapter mAdapter;
    private GlassesAdapter mGlassesAdapter;
    private int mMemberRole = Member.IS_NOT_A_MEMBER;

    private AppCompatButton btnRight, btnLeft;
    private View layoutAction, layoutEdit;
    private AppCompatTextView groupNameTv, groupCountMemberTv, groupCharacterTv;
    private AppCompatButton btnInviteMember;
    private RecyclerView rvGlasses;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_group_details;
    }

    @Override
    protected IGroupViewModel getViewModel() {
        return new GroupViewModel(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String id = intent.getStringExtra(GROUP_ID);
        String currentID = this.getIntent().getStringExtra(GROUP_ID);
        if (id != null && !id.equals(currentID)) {
            this.getIntent().putExtra(GROUP_ID, id);
            recreate();
        }
    }

    @Override
    protected void initView() {
        groupNameTv = findViewById(R.id.tv_group_name);
        groupCountMemberTv = findViewById(R.id.tv_group_count_member);
        groupCharacterTv = findViewById(R.id.tv_group_short_name);
        btnInviteMember = findViewById(R.id.btn_invite_member);
        btnRight = findViewById(R.id.btn_right);
        btnLeft = findViewById(R.id.btn_left);
        layoutAction = findViewById(R.id.layout_group_action);
        layoutEdit = findViewById(R.id.layout_edit);
        AppCompatButton btnEdit = findViewById(R.id.btn_edit);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_member);
        mRecyclerView.setNestedScrollingEnabled(false);
        rvGlasses = findViewById(R.id.recycler_view_glasses);

        groupNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGroup();
            }
        });

        btnRight.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnInviteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group group = mViewModel.myRequestGroup().getValue();
                new InviteMemberDialog(group.getId(), group.getName(), new OnRequestSuccess<String>() {
                    @Override
                    public void onSuccess(String data) {
                        mViewModel.getMembers(data);
                    }
                }).show(getSupportFragmentManager(), null);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGroup();
            }
        });

        rvGlasses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new MemberAdapter(this, new ArrayList<Member>(), MemberAdapter.GROUP_MEMBER, new MemberAdapter.OnMemberClickListener() {
            @Override
            public void onMemberClicked(Member member) {
                new MemberDetailsFragment(Objects.requireNonNull(mViewModel.myRequestGroup().getValue()).getId(), member,
                        mMemberRole == Member.ADMIN)
                        .show(getSupportFragmentManager(), null);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateGroup() {
        if (mMemberRole == Member.ADMIN) {
            new UpdateGroupDialog(GroupDetailsActivity.this, groupNameTv.getText().toString(), new OnRequestSuccess<String>() {
                @Override
                public void onSuccess(String data) {
                    mViewModel.updateGroupName(data);
                }
            }).show();
        }
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.getGroupDetails(getIntent().getStringExtra(GROUP_ID));
        mViewModel.myRequestGroup().observe(this, new Observer<Group>() {
            @Override
            public void onChanged(Group group) {
                if (group == null) {
                    finish();
                    return;
                }
                setGroupData(group);
            }
        });
    }

    private void setGroupData(Group mGroup) {
        if (mGroup != null) {
            groupNameTv.setText(mGroup.getName());
            groupCharacterTv.setText(mGroup.getFirstCharacter());
            groupCountMemberTv.setText(mGroup.getCountMemberText(this));
            mAdapter.setList(mGroup.getMembers());
            if (mGroup.getMembers() != null) {
                checkRole(mGroup.getMembers(), mGroup.getGlasses());
            }
        }
    }

    private void checkRole(ArrayList<Member> members, ArrayList<Glasses> glasses) {
        Profile profile = Prefs.getInstance(this).getProfile();
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member.getId().equals(profile.getId())) {
                mMemberRole = member.getRole();
                break;
            }
        }

        boolean isHasGlass = glasses != null && !glasses.isEmpty();

        layoutAction.setVisibility(View.VISIBLE);

        if (mMemberRole == Member.ADMIN) {
            btnInviteMember.setVisibility(View.VISIBLE);
            layoutEdit.setVisibility(View.VISIBLE);
            btnLeft.setVisibility(View.VISIBLE);
            btnLeft.setText(getString(R.string.btn_remove_group));
            btnRight.setVisibility(View.GONE);
        } else {
            layoutEdit.setVisibility(View.GONE);
            btnInviteMember.setVisibility(View.GONE);
            btnRight.setVisibility(View.VISIBLE);
            if (mMemberRole == Member.MEMBER) {
                btnLeft.setVisibility(View.GONE);
                btnRight.setText(getString(R.string.btn_leave_group));
            } else if (mMemberRole == Member.IVITING_MEMBER) {
                btnLeft.setVisibility(View.VISIBLE);
                btnLeft.setText(getString(R.string.btn_reject));
                btnRight.setText(getString(R.string.btn_accept));
            } else {
                btnLeft.setVisibility(View.GONE);
                btnRight.setText(getString(R.string.btn_join_group));
            }
        }

        mGlassesAdapter = new GlassesAdapter(this, isHasGlass ? glasses : new ArrayList<Glasses>(),
                mMemberRole == Member.ADMIN, new GlassesAdapter.OnGlassesClickListener() {
            @Override
            public void onAddGlassesClicked() {
                new CreateGlassesDialog(getIntent().getStringExtra(GROUP_ID), new CreateGlassesDialog.OnCreateGlassesListener() {
                    @Override
                    public void onCreatedGlasses() {
                        mViewModel.getGlasses(mViewModel.myRequestGroup().getValue().getId());
                    }
                }).show(GroupDetailsActivity.this.getSupportFragmentManager(), null);
            }

            @Override
            public void onGlassesClicked(Glasses glasses) {
                Intent intent = new Intent(getApplicationContext(), GlassesDetailsActivity.class);
                intent.putExtra(GlassesDetailsActivity.GLASSES_DATA, glasses);
                intent.putExtra(IS_FROM_ADMIN, mMemberRole == Member.ADMIN);
                startActivityForResult(intent, 1);
            }
        });
        rvGlasses.setAdapter(mGlassesAdapter);
    }


    @Override
    public void onClick(View v) {
        if (v instanceof AppCompatButton) {
            String btnName = ((AppCompatButton) v).getText().toString().trim();
            final Group group = mViewModel.myRequestGroup().getValue();
            if (group != null) {
                if (btnName.equals(getString(R.string.btn_join_group))) {
                    Toast.makeText(this, "Join group here!", Toast.LENGTH_SHORT).show();
                } else if (btnName.equals(getString(R.string.btn_leave_group))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.msg_leave_group));
                    builder.setCancelable(false);
                    builder.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            mViewModel.leaveGroup(group.getId());
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
                } else if (btnName.equals(getString(R.string.btn_accept))) {
                    mViewModel.confirmInvitations(group.getId(), true);
                } else if (btnName.equals(getString(R.string.btn_reject))) {
                    mViewModel.confirmInvitations(group.getId(), false);
                } else if (btnName.equals(getString(R.string.btn_remove_group))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.msg_remove_group));
                    builder.setCancelable(false);
                    builder.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            mViewModel.removeGroup(group);
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
            }
        }
    }

    public void onRemoveMember(String memberID) {
        mViewModel.removeMember(null, memberID, null);
    }

    public void onChangeMemberRole(String memberID, int role) {
        mViewModel.changeMemberRole(null, memberID, role, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mViewModel.getGroupDetails(getIntent().getStringExtra(GROUP_ID));
        }
    }
}
