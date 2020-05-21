package com.example.familyapp.view.main.group;

import com.example.familyapp.R;
import com.example.familyapp.model.Group;
import com.example.familyapp.model.Member;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.view.main.member.MemberAdapter;
import com.example.familyapp.viewmodel.base.IViewModel;

import java.util.ArrayList;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchGroupActivity extends BaseActivity<IViewModel> {

    public static final String GROUP_DATA = "group_data";

    private RecyclerView mRecyclerView;
    private GroupAdapter mAdapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_join_group;
    }

    @Override
    protected IViewModel getViewModel() {
        return null;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.rcl_group);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new GroupAdapter(this, getGroups(), GroupAdapter.SEARCH_GROUP, null);
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<Group> getGroups() {
        ArrayList<Group> groups = new ArrayList<>();
        groups.add(new Group("BC13", new ArrayList<Member>()));
        groups.add(new Group("English", new ArrayList<Member>()));
        groups.add(new Group("Python", new ArrayList<Member>()));
        groups.add(new Group("Kotlin", new ArrayList<Member>()));
        groups.add(new Group("Machine Learning", new ArrayList<Member>()));
        return groups;
    }
}
