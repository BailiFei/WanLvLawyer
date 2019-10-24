package com.lawyer.controller.cases;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.databinding.ItemCaseProcessBinding;
import com.lawyer.entity.CaseProgressBean;
import com.lawyer.entity.UserCaseBean;
import com.lawyer.mvvm.adapter.recyclerview.RecyclerViewAdapter;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;

import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.img.IMGLoad;
import ink.itwo.common.util.CollectionUtil;
import ink.itwo.common.util.StringUtils;
import ink.itwo.common.widget.baseViewholder.CommonAdapter;
import ink.itwo.common.widget.baseViewholder.ViewHolder;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 案件进程
 @author wang
 on 2019/2/12 */

public class CasesProcessFm extends BaseFragment<MainActivity> {
    public static CasesProcessFm newInstance(UserCaseBean bean) {

        Bundle args = new Bundle();

        CasesProcessFm fragment = new CasesProcessFm();
        args.putParcelable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }

    private UserCaseBean bean;
    private SwipeRefreshLayout refreshLayout;
    private List<CaseProgressBean> data = new ArrayList<>();
    private int pageNum;
    private RecyclerViewAdapter<CaseProgressBean, ItemCaseProcessBinding> adapter = new RecyclerViewAdapter<CaseProgressBean, ItemCaseProcessBinding>(R.layout.item_case_process, data) {
        @Override
        protected void convert(ItemCaseProcessBinding bind, CaseProgressBean item) {
            super.convert(bind, item);
            bind.gridView.setAdapter(new CommonAdapter<String>(mActivity, StringUtils.splitForList(item.getPicUrl(), "\\|"), R.layout.item_image) {
                @Override
                public void convert(ViewHolder holder, String item) {
                    ImageView imageView = holder.getView(R.id.image);
                    IMGLoad.with(CasesProcessFm.this).load(item).into(imageView);
                }
            });
        }
    };

    @Override
    protected int initLayoutId() {
        return R.layout.fm_case_process;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        bean = getArguments().getParcelable("bean");
        refreshLayout = findViewById(R.id.refresh_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnLoadMoreListener(this::getInfo, recyclerView);
        refreshLayout.setOnRefreshListener(this::onRefresh);
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
        if (bean == null) return;
        //没分页？
        if (pageNum > 0) {
            adapter.loadMoreEnd();
            return;
        }
        NetManager.http().create(API.class)
                .caseProgress(UserCache.getAccessToken(), bean.getId(), pageNum)
                .compose(NetTransformer.handle(this))
                .subscribe(new ResultObserver<Result<List<CaseProgressBean>>>() {
                    @Override
                    public void onNext(Result<List<CaseProgressBean>> result) {
                        List<CaseProgressBean> list = result.getData();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        pageNum++;
                        data.addAll(list);
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
