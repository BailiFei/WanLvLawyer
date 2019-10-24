package com.lawyer.controller.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.base.BaseFragment;
import com.lawyer.databinding.ItemMessageBinding;
import com.lawyer.entity.MessageBean;
import com.lawyer.entity.MessageBeanExt;
import com.lawyer.mvvm.adapter.recyclerview.RecyclerViewAdapter;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;
import com.lawyer.util.UserCache;

import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.util.CollectionUtil;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 消息通知
 @author wang
 on 2019/3/18 */

public class MessageFm extends BaseFragment<MainActivity> {
    public static MessageFm newInstance() {

        Bundle args = new Bundle();

        MessageFm fragment = new MessageFm();
        fragment.setArguments(args);
        return fragment;
    }

    private SwipeRefreshLayout refreshLayout;
    private List<MessageBean> data = new ArrayList<>();
    private int pageNum;
    private RecyclerViewAdapter<MessageBean, ItemMessageBinding> adapter = new RecyclerViewAdapter<MessageBean, ItemMessageBinding>(R.layout.item_message, data) {
        @Override
        protected void convert(ItemMessageBinding bind, MessageBean item) {
            super.convert(bind, item);
        }
    };

    @Override
    protected int initLayoutId() {
        return R.layout.fm_message;
    }

    @Override
    public void onCreateView(View viewRoot) {
        super.onCreateView(viewRoot);
        setSwipeBackEnable(true);
        setTitle("消息通知");
        refreshLayout = findViewById(R.id.refresh_layout);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setOnLoadMoreListener(this::getInfo, recyclerView);
        adapter.setEmptyView(R.layout.empty_message);
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
        NetManager.http().create(API.class)
                .messageList(UserCache.getAccessToken(), pageNum, 20)
                .compose(NetTransformer.handle(MessageFm.this))
                .subscribe(new ResultObserver<Result<MessageBeanExt>>() {
                    @Override
                    public void onNext(Result<MessageBeanExt> result) {
                        MessageBeanExt ext = result.getData();
                        if (ext == null) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        List<MessageBean> list = ext.getList();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        pageNum++;
                        MessageFm.this.data.addAll(list);
                        adapter.loadMoreComplete();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
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
