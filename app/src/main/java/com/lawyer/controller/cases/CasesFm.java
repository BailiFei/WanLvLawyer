package com.lawyer.controller.cases;

import android.os.Bundle;
import android.support.design.widget.ExtendTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.entity.UserCaseBean;

import java.util.ArrayList;
import java.util.List;

/**
 委托案件详情及进程
 @author wang
 on 2019/2/21 */

public class CasesFm extends BaseFragment<MainActivity> {
    public static CasesFm newInstance(UserCaseBean bean) {

        Bundle args = new Bundle();

        CasesFm fragment = new CasesFm();
        args.putParcelable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }

    private ViewPager viewPager;
    private ExtendTabLayout tabLayout;

    @Override
    protected int initLayoutId() {
        return R.layout.fm_cases;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setSwipeBackEnable(true);
        tabLayout = viewRoot.findViewById(R.id.tab_layout);
        viewPager = viewRoot.findViewById(R.id.view_pager);
        String[] titles = {"案件详情", "案件进程"};
        UserCaseBean bean = getArguments().getParcelable("bean");
        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(CasesDetailFm.newInstance(bean,false));
        fragments.add(CasesProcessFm.newInstance(bean));
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
