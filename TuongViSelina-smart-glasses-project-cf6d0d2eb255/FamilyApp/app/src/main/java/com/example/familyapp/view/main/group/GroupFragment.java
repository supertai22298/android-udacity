package com.example.familyapp.view.main.group;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import com.example.familyapp.R;
import com.example.familyapp.view.base.BaseFragment;
import com.example.familyapp.viewmodel.group.GroupViewModel;
import com.example.familyapp.viewmodel.group.IGroupViewModel;


public class GroupFragment extends BaseFragment<IGroupViewModel> {

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_home_group;
    }

    @Override
    protected IGroupViewModel getViewModel() {
        return new GroupViewModel(getContext());
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final ViewPager pager = view.findViewById(R.id.view_pager_group);
        final AppCompatButton btnMyGroup = view.findViewById(R.id.btn_my_group);
        final AppCompatButton btnRequestToJoin = view.findViewById(R.id.btn_request_join_group);

        btnMyGroup.setSelected(true);
        btnRequestToJoin.setSelected(false);

        GroupPagerAdapter adapter = new GroupPagerAdapter(getChildFragmentManager());

        pager.setOffscreenPageLimit(2);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                btnMyGroup.setSelected(position == 0);
                btnRequestToJoin.setSelected(position == 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setAdapter(adapter);

        btnMyGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
            }
        });

        btnRequestToJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
            }
        });
    }

}
