package com.lawyer.controller.cases;

import android.os.Bundle;
import android.support.design.widget.ExtendTabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.databinding.ItemCaseEntrustBinding;
import com.lawyer.entity.UserCaseBean;
import com.lawyer.enums.CaseStateEnum;
import com.lawyer.mvvm.adapter.recyclerview.RecyclerViewAdapter;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;

import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.util.CollectionUtil;
import ink.itwo.common.util.DeviceUtil;
import ink.itwo.common.view.Divider;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 用户案件委托列表
 @author wang
 on 2019/2/12 */

public class CasesEntrustFm extends BaseFragment<MainActivity> {
    public static CasesEntrustFm newInstance() {

        Bundle args = new Bundle();

        CasesEntrustFm fragment = new CasesEntrustFm();
        fragment.setArguments(args);
        return fragment;
    }

    private int pageNum;
    private List<UserCaseBean> data = new ArrayList<>();
    private CaseStateEnum[] titles = {CaseStateEnum.tendering, CaseStateEnum.solving, CaseStateEnum.done};
    private RecyclerViewAdapter<UserCaseBean, ItemCaseEntrustBinding> adapter = new RecyclerViewAdapter<UserCaseBean, ItemCaseEntrustBinding>(R.layout.item_case_entrust, data){
        @Override
        protected void convert(ItemCaseEntrustBinding bind, UserCaseBean item) {
            super.convert(bind, item);
            bind.getRoot().setOnClickListener(v->mActivity.start(CasesFm.newInstance(item)));
        }
    };
    private SwipeRefreshLayout refreshLayout;
    private CaseStateEnum stateEnum = CaseStateEnum.tendering;

    @Override
    protected int initLayoutId() {
        return R.layout.fm_case_entrust;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("案件委托");
        setSwipeBackEnable(true);
        refreshLayout = findViewById(R.id.refresh_layout);
        ExtendTabLayout tabLayout = viewRoot.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(titles[0].getTitle()));
        tabLayout.addTab(tabLayout.newTab().setText(titles[1].getTitle()));
        tabLayout.addTab(tabLayout.newTab().setText(titles[2].getTitle()));
        RecyclerView recyclerView = viewRoot.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(Divider.newBuilder()
                .put(Divider.top, DeviceUtil.getDimensionPx(R.dimen.dp_20))
                .build());
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnLoadMoreListener(this::getInfo, recyclerView);
        refreshLayout.setOnRefreshListener(this::onRefresh);
        tabLayout.addOnTabSelectedListener(new ExtendTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(ExtendTabLayout.Tab tab) {
                stateEnum = titles[tab.getPosition()];
                onRefresh();
            }

            @Override
            public void onTabUnselected(ExtendTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(ExtendTabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        onRefresh();
    }

    private void onRefresh() {
        refreshLayout.setRefreshing(true);
        data.clear();
        pageNum = 0;
        adapter.notifyDataSetChanged();
        getInfo();
    }

    private void getInfo() {
        NetManager.http().create(API.class)
                .caseCaseList(UserCache.getAccessToken(), stateEnum, pageNum)
                .compose(NetTransformer.handle(this))
                .subscribe(new ResultObserver<Result<List<UserCaseBean>>>() {
                    @Override
                    public void onNext(Result<List<UserCaseBean>> result) {
                        if (result == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        List<UserCaseBean> list = result.getData();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        pageNum++;
                        data.addAll(list);
                        if (pageNum == result.getTotalPage()) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        adapter.loadMoreComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

}
