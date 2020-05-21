package com.example.familyapp.view.main;

import com.example.familyapp.view.main.account.AccountFragment;
import com.example.familyapp.view.main.group.GroupFragment;
import com.example.familyapp.view.main.home.HomeFragment;
import com.example.familyapp.view.main.sos.SOSNotificationFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>() {{
        add(new HomeFragment());
        add(new GroupFragment());
        add(new SOSNotificationFragment());
        add(new AccountFragment());
    }};
    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public MainPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
