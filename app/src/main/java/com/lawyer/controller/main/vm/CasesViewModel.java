package com.lawyer.controller.main.vm;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.lawyer.R;
import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.main.MainCaseFm;
import com.lawyer.controller.main.head.CasesHeadView;
import com.lawyer.databinding.FmMainCaseBinding;
import com.lawyer.databinding.ItemAdvisoryCaseBinding;
import com.lawyer.entity.CaseExampleBean;
import com.lawyer.entity.CaseExampleExt;
import com.lawyer.mvvm.adapter.recyclerview.BaseBindingViewHolder;
import com.lawyer.mvvm.adapter.recyclerview.RecyclerViewAdapter;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.net.Url;

import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.util.CollectionUtil;
import ink.itwo.common.util.DeviceUtil;
import ink.itwo.common.view.Divider;
import ink.itwo.common.view.OnRVItemClickListener;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 @author wang
 on 2019/2/25 */

public class CasesViewModel extends AbsViewModel<MainCaseFm, FmMainCaseBinding> {
    private int pageNum = 0;
    private Integer castType;
    private List<CaseExampleBean> data = new ArrayList<>();
    private RecyclerViewAdapter<CaseExampleBean, ItemAdvisoryCaseBinding> adapter = new RecyclerViewAdapter<CaseExampleBean, ItemAdvisoryCaseBinding>(R.layout.item_advisory_case, data) {
        @Override
        protected void convert(BaseBindingViewHolder<ItemAdvisoryCaseBinding> helper, CaseExampleBean item) {
            super.convert(helper, item);
            helper.getBinding().layout.setOnClickListener(new OnClick(helper, item));
        }
    };
    private CasesHeadView headView;
    @Bindable
    public ObservableBoolean refresh = new ObservableBoolean(false);
    public SwipeRefreshLayout.OnRefreshListener refreshListener = this::refresh;

    public CasesViewModel(MainCaseFm mainCaseFm, FmMainCaseBinding fmMainCaseBinding) {
        super(mainCaseFm, fmMainCaseBinding);
        bind.recyclerView.addItemDecoration(Divider.newBuilder()
                .put(Divider.top, DeviceUtil.getDimensionPx(R.dimen.dp_20))
                .build());
        adapter.bindToRecyclerView(bind.recyclerView);
        adapter.setOnLoadMoreListener(this::getInfo, bind.recyclerView);
        headView = new CasesHeadView(getView().getActivity());
        headView.setListener(item -> {
            castType = item.getId();
            refresh();
        });
        adapter.addHeaderView(headView);
    }

    private void refresh() {
        pageNum = 0;
        data.clear();
        adapter.notifyDataSetChanged();
        refresh.set(true);
        getInfo();
    }

    public void getInfo() {
        NetManager.http().create(API.class)
                .caseExampleList(/*UserCache.getAccessToken(),*/ pageNum, castType)
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<CaseExampleExt>>() {
                    @Override
                    public void onNext(Result<CaseExampleExt> result) {
                        CaseExampleExt ext = result.getData();
                        if (ext == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        headView.setData(ext.getCaseTypeList());
                        List<CaseExampleBean> list = ext.getList();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        pageNum++;
                        data.addAll(list);
                        adapter.loadMoreComplete();
                    }

                    @Override
                    public void onComplete() {
                        refresh.set(false);
                    }
                });
    }

    private class OnClick extends OnRVItemClickListener<CaseExampleBean> {

        @Override
        public void onClick(View v) {
            onSkip.put(1, Url.h5CaseExampleDetail(item.getId()));
        }

        public OnClick(BaseViewHolder helper, CaseExampleBean caseExampleBean) {
            super(helper, caseExampleBean);
        }
    }

}
