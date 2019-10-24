package com.lawyer.controller.main.vm;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.main.MainWelfareFm;
import com.lawyer.databinding.FmMainWelfareBinding;
import com.lawyer.databinding.ItemWelfareBinding;
import com.lawyer.databinding.ItemWelfareHeadBinding;
import com.lawyer.entity.WelfareBean;
import com.lawyer.entity.WelfareBeanExt;
import com.lawyer.mvvm.adapter.recyclerview.RecyclerViewAdapter;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.util.CollectionUtil;
import ink.itwo.common.util.DeviceUtil;
import ink.itwo.common.view.Divider;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 @author wang
 on 2019/2/26 */

public class WelfareViewModel extends AbsViewModel<MainWelfareFm, FmMainWelfareBinding> {
    private int pageNum;
    private List<WelfareBean> datas = new ArrayList<>();
    private RecyclerViewAdapter<WelfareBean, ItemWelfareBinding> adapter = new RecyclerViewAdapter<WelfareBean, ItemWelfareBinding>(R.layout.item_welfare, datas) {
        @Override
        protected void convert(ItemWelfareBinding bind, WelfareBean item) {
            super.convert(bind, item);
            bind.getRoot().setOnClickListener(v -> onSkip.put(1, item));
            bind.toPay.setOnClickListener(v->onSkip.put(2,item));
        }
    };

    @Bindable
    public ObservableBoolean refresh = new ObservableBoolean(false);
    private WelfareHeadViewModel headViewModel = new WelfareHeadViewModel();
    @Bindable
    public SwipeRefreshLayout.OnRefreshListener refreshListener = this::onRefresh;

    public WelfareViewModel(MainWelfareFm mainWelfareFm, FmMainWelfareBinding fmMainWelfareBinding) {
        super(mainWelfareFm, fmMainWelfareBinding);
        ItemWelfareHeadBinding headBind = DataBindingUtil.inflate(LayoutInflater.from(getView().getContext()), R.layout.item_welfare_head, null, false);
        headBind.setVariable(BR.bean, headViewModel);
        adapter.addHeaderView(headBind.getRoot());
        adapter.bindToRecyclerView(this.bind.recyclerView);
        this.bind.recyclerView.addItemDecoration(Divider.newBuilder()
                .put(Divider.top, DeviceUtil.getDimensionPx(R.dimen.dp_30))
                .build());
        adapter.setOnLoadMoreListener(this::getInfo, this.bind.recyclerView);
    }

    private void onRefresh() {
        refresh.set(true);
        pageNum = 0;
        datas.clear();
        adapter.notifyDataSetChanged();
        getInfo();
    }

    @Override
    public void getInfo() {
        NetManager.http().create(API.class)
                .projectList(/*UserCache.getAccessToken(),*/ pageNum)
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<WelfareBeanExt>>() {
                    @Override
                    public void onNext(Result<WelfareBeanExt> result) {
                        WelfareBeanExt ext = result.getData();
                        if (ext == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        headViewModel.totalMoney.set(NumberFormat.getNumberInstance().format(ext.getTotalMoney()));
                        headViewModel.totalHelpCount.set(NumberFormat.getNumberInstance().format(ext.getTotalHelpCount()));
                        headViewModel.totalHelpPeole.set(NumberFormat.getNumberInstance().format(ext.getTotalHelpPeole()));

                        List<WelfareBean> list = ext.getList();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        datas.addAll(list);
                        pageNum++;
                        if (pageNum == result.getTotalPage()) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        adapter.loadMoreComplete();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        refresh.set(false);
                    }
                });
    }
}
