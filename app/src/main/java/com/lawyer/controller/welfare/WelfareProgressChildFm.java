package com.lawyer.controller.welfare;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.databinding.ItemWelfareProgressChildBinding;
import com.lawyer.entity.WelfareBean;
import com.lawyer.entity.WelfareProgressBean;
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
 公益详情-项目进展
 @author wang
 on 2019/2/14 */

public class WelfareProgressChildFm extends BaseFragment<MainActivity> {
    public static WelfareProgressChildFm newInstance(WelfareBean bean) {

        Bundle args = new Bundle();

        WelfareProgressChildFm fragment = new WelfareProgressChildFm();
        args.putParcelable("bean", bean);
        fragment.setArguments(args);
        return fragment;
    }

    private List<WelfareProgressBean> datas = new ArrayList<>();
    private int pageNum, id;
    private RecyclerViewAdapter<WelfareProgressBean, ItemWelfareProgressChildBinding> adapter;
    @Override
    protected int initLayoutId() {
        return R.layout.fm_common_recycler;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        id = ((WelfareBean) getArguments().getParcelable("bean")).getId();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setBackgroundColor(Color.WHITE);
        adapter = new RecyclerViewAdapter<>(R.layout.item_welfare_progress_child, datas);
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
                .projectProgressList(/*UserCache.getAccessToken(),*/ id, pageNum)
                .compose(NetTransformer.handle(this))
                .subscribe(new ResultObserver<Result<JsonObject>>() {
                    @Override
                    public void onNext(Result<JsonObject> result) {
                        if (result == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        JsonObject jsonObject = result.getData();
                        if (jsonObject == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        JsonArray jsonArray = jsonObject.getAsJsonArray("list");
                        if (jsonArray == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        List<WelfareProgressBean> list = new Gson().fromJson(jsonArray, new TypeToken<List<WelfareProgressBean>>() {
                        }.getType());
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        pageNum++;
                        datas.addAll(list);
                        if (pageNum == result.getTotalPage()) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        adapter.loadMoreComplete();
                    }
                });
    }
}
