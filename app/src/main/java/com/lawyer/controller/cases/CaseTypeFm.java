package com.lawyer.controller.cases;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.databinding.ItemAdvisoryCaseTypeBinding;
import com.lawyer.delegate.Moor7;
import com.lawyer.entity.CaseTypeBean;
import com.lawyer.mvvm.adapter.recyclerview.RecyclerViewAdapter;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;

import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.util.CollectionUtil;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 全部按类型
 @author wang
 on 2019/3/4 */

public class CaseTypeFm extends BaseFragment<MainActivity> {
    public static CaseTypeFm newInstance() {

        Bundle args = new Bundle();

        CaseTypeFm fragment = new CaseTypeFm();
        fragment.setArguments(args);
        return fragment;
    }

    private SwipeRefreshLayout refreshLayout;
    private List<CaseTypeBean> data = new ArrayList<>();
    private int pageNum;
    private Moor7 moor7;
    private RecyclerViewAdapter<CaseTypeBean, ItemAdvisoryCaseTypeBinding> adapter
            = new RecyclerViewAdapter<CaseTypeBean, ItemAdvisoryCaseTypeBinding>(R.layout.item_advisory_case_type, data) {
        @Override
        protected void convert(ItemAdvisoryCaseTypeBinding bind, CaseTypeBean item) {
            super.convert(bind, item);
            bind.layout.setOnClickListener(v -> {
                if (moor7 == null) {
                    moor7 = new Moor7(CaseTypeFm.this);
                }
                moor7.online();
            });
        }
    };

    @Override
    protected int initLayoutId() {
        return R.layout.fm_case_type;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setTitle("全部功能");
        refreshLayout = findViewById(R.id.refresh_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 4));
        adapter.bindToRecyclerView(recyclerView);
//        adapter.setOnLoadMoreListener(this::getInfo, recyclerView);
        refreshLayout.setOnRefreshListener(this::onRefresh);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (moor7 != null) {
            moor7 = null;
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getInfo();
    }

    private void onRefresh() {
        pageNum = 0;
        data.clear();
        adapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(true);
        getInfo();
    }

    private void getInfo() {
        NetManager.http().create(API.class)
                .caseVideoCaseType(/*UserCache.getAccessToken(),*/ pageNum)
                .compose(NetTransformer.handle(this))
                .subscribe(new ResultObserver<Result<List<CaseTypeBean>>>() {
                    @Override
                    public void onNext(Result<List<CaseTypeBean>> result) {
                        List<CaseTypeBean> list = result.getData();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.notifyDataSetChanged();
                            return;
                        }
                        pageNum++;
                        adapter.addData(list);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
//                        adapter.loadMoreFail();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

}
