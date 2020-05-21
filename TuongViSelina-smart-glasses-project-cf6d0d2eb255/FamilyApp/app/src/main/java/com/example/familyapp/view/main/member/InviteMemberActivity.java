package com.example.familyapp.view.main.member;

import android.util.Log;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.model.Group;
import com.example.familyapp.model.Member;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.viewmodel.base.IViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InviteMemberActivity extends BaseActivity<IViewModel> {

    public static final String GROUP_DATA = "group_data";

    private RecyclerView mRecyclerView;
    private MemberAdapter mAdapter;
    private AppCompatEditText edtSearchMember;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_invite_member;
    }

    @Override
    protected IViewModel getViewModel() {
        return null;
    }

    @Override
    protected void initView() {
        edtSearchMember = findViewById(R.id.edt_search_member);
        mRecyclerView = findViewById(R.id.rcl_members);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MemberAdapter(this, getMembers(), MemberAdapter.INVITE_MEMBER, new MemberAdapter.OnMemberClickListener() {
            @Override
            public void onMemberClicked(Member member) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<Member> getMembers() {
        return new ArrayList<Member>() {{
            add(new Member("1", "Miller", "0787 664 331", Member.MEMBER));
            add(new Member("2", "Will", "0356 356 356", Member.MEMBER));
            add(new Member("3", "Trixie", "0123 456 456", Member.MEMBER));
            add(new Member("4", "Mitt", "0908 809 908", Member.MEMBER));
            add(new Member("5", "Howie", "0898 109 109", Member.ADMIN));
        }};
    }
}
