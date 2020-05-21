package com.example.familyapp.view.main.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.model.Group;
import com.example.familyapp.view.base.BaseFragment;
import com.example.familyapp.viewmodel.group.GroupViewModel;
import com.example.familyapp.viewmodel.group.IGroupViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupListFragment extends BaseFragment<IGroupViewModel> {

    private static final String GROUP_TYPE = "group_type";

    private GroupAdapter mAdapter;
    private AppCompatTextView tvEmptyGroup;
    private int mGroupListType = GroupAdapter.MY_GROUP;

    public static GroupListFragment newInstance(int type) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(GROUP_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_group_list;
    }

    @Override
    protected IGroupViewModel getViewModel() {
        return new GroupViewModel(getContext());
    }

    @Override
    protected void initView(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        tvEmptyGroup = view.findViewById(R.id.tv_empty);
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        View layoutAction = view.findViewById(R.id.layout_group_action);
        AppCompatButton btnCreateGroup = view.findViewById(R.id.btn_add_group);
        AppCompatButton btnJoinGroup = view.findViewById(R.id.btn_join_group);

        mGroupListType = getArguments() != null ? getArguments().getInt(GROUP_TYPE) : GroupAdapter.MY_GROUP;

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new GroupAdapter(getContext(), new ArrayList<Group>(), mGroupListType, new GroupAdapter.OnGroupClickListener() {
            @Override
            public void onGroupClicked(Group group) {
                Intent intent = new Intent(getContext(), GroupDetailsActivity.class);
                intent.putExtra(GroupDetailsActivity.GROUP_ID, group.getId());
                startActivity(intent);//dang vi du
            }

            @Override
            public void onActionClicked(Group group, int type, boolean isAccepted) {
                switch (type) {
                    case GroupAdapter.SEARCH_GROUP: {
                        mViewModel.requestJoinGroup(group.getId());
                        break;
                    }
                    default:
                        mViewModel.confirmInvitations(group.getId(), isAccepted);
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        layoutAction.setVisibility(mGroupListType == GroupAdapter.MY_GROUP ? View.VISIBLE : View.GONE);

        tvEmptyGroup.setText(getString(mGroupListType == GroupAdapter.MY_GROUP ? R.string.tv_empty_group : R.string.tv_empty_request_to_join));

        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateGroupDialog(new CreateGroupDialog.OnCreateGroupListener() {
                    @Override
                    public void onCreatedGroup(Group group) {
                        mAdapter.addGroup(group);
                        tvEmptyGroup.setVisibility(View.GONE);
                    }
                }).show(getChildFragmentManager(), null);
            }
        });

        btnJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchGroupActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.getGroups(mGroupListType);
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.myGroups().observe(getViewLifecycleOwner(), new Observer<ArrayList<Group>>() {
            @Override
            public void onChanged(ArrayList<Group> groups) {
                mAdapter.setGroupList(groups);
                tvEmptyGroup.setVisibility(groups.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void showLoading(boolean isShow) {
        if (isShow && mViewModel.myGroups().getValue() != null) {
            return;
        }
        super.showLoading(isShow);
    }
}
