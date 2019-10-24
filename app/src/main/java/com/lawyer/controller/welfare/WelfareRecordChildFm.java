package com.lawyer.controller.welfare;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.databinding.ItemWelfareRecordChildBinding;
import com.lawyer.entity.DataExt;
import com.lawyer.entity.WelfareBean;
import com.lawyer.entity.WelfareRecordBean;
import com.lawyer.mvvm.adapter.recyclerview.RecyclerViewAdapter;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;

import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.util.CollectionUtil;
import ink.itwo.common.util.DeviceUtil;
import ink.itwo.common.view.Divider;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 公益详情-项目记录
 @author wang
 on 2019/2/14 */

public class WelfareRecordChildFm extends BaseFragment<MainActivity> {
    public static WelfareRecordChildFm newInstance(WelfareBean bean) {
        Bundle args = new Bundle();
        WelfareRecordChildFm fragment = new WelfareRecordChildFm();
        args.putParcelable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }

    private List<WelfareRecordBean> datas = new ArrayList<>();
    private RecyclerViewAdapter<WelfareRecordBean, ItemWelfareRecordChildBinding> adapter;
    private int pageNum, id;

    @Override
    protected int initLayoutId() {
        return R.layout.fm_common_recycler;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        id = ((WelfareBean) getArguments().getParcelable("bean")).getId();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setBackgroundColor(Color.WHITE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(Divider.newBuilder().put(Divider.bottom, DeviceUtil.getDimensionPx(R.dimen.dp_20)).build());
        adapter = new RecyclerViewAdapter<>(R.layout.item_welfare_record_child, datas);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnLoadMoreListener(this::getInfo, recyclerView);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getInfo();
    }

    private void getInfo() {
        NetManager.http().create(API.class)
                .projectLogList(/*UserCache.getAccessToken(),*/ id, pageNum)
                .compose(NetTransformer.handle(this))
                .subscribe(new ResultObserver<Result<DataExt<WelfareRecordBean>>>() {
                    @Override
                    public void onNext(Result<DataExt<WelfareRecordBean>> result) {
                        if (result == null || result.getData() == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        List<WelfareRecordBean> list = result.getData().getList();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        pageNum++;
                        datas.addAll(list);
                        adapter.loadMoreComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        adapter.loadMoreFail();
                    }
                });
    }


}
