package com.example.familyapp.view.main.group;

import com.example.familyapp.view.base.BaseFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class GroupPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<GroupListFragment> fragments = new ArrayList<>();
    public GroupPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        GroupListFragment fragment = GroupListFragment.newInstance(position);
        fragments.add(fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
