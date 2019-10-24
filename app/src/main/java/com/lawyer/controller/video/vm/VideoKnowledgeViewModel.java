package com.lawyer.controller.video.vm;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.support.v4.widget.SwipeRefreshLayout;

import com.lawyer.R;
import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.video.VideoKnowledgeFm;
import com.lawyer.databinding.FmVideoKnowledgeBinding;
import com.lawyer.databinding.ItemVideoKnowledgeBinding;
import com.lawyer.entity.CaseKnowledgeBean;
import com.lawyer.entity.CaseKnowledgeExt;
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
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 @author wang
 on 2019/2/26 */

public class VideoKnowledgeViewModel extends AbsViewModel<VideoKnowledgeFm, FmVideoKnowledgeBinding> {
    private int pageNum = 0;
    private List<CaseKnowledgeBean> datas = new ArrayList<>();
    @Bindable
    public ObservableBoolean isRefreshing = new ObservableBoolean(false);
    private RecyclerViewAdapter<CaseKnowledgeBean, ItemVideoKnowledgeBinding> adapter = new RecyclerViewAdapter<CaseKnowledgeBean, ItemVideoKnowledgeBinding>(R.layout.item_video_knowledge, datas){
        @Override
        protected void convert(ItemVideoKnowledgeBinding bind, CaseKnowledgeBean item) {
            super.convert(bind, item);
            bind.getRoot().setOnClickListener(v->onSkip.put(1,Url.h5CaseKnowledgeDetail(item.getId())));
        }
    };
    @Bindable
    public SwipeRefreshLayout.OnRefreshListener refreshListener = this::onRefresh;

    public VideoKnowledgeViewModel(VideoKnowledgeFm videoKnowledgeFm, FmVideoKnowledgeBinding fmVideoKnowledgeBinding) {
        super(videoKnowledgeFm, fmVideoKnowledgeBinding);
        bind.recyclerView.addItemDecoration(Divider.newBuilder()
                .put(Divider.bottom, DeviceUtil.getDimensionPx(R.dimen.dp_30))
                .build());
        adapter.bindToRecyclerView(bind.recyclerView);
        adapter.setOnLoadMoreListener(this::getInfo, bind.recyclerView);
    }

    private void onRefresh() {
        isRefreshing.set(true);
        pageNum = 0;
        datas.clear();
        adapter.notifyDataSetChanged();
        getInfo();
    }

    @Override
    public void getInfo() {
        NetManager.http().create(API.class)
                .caseKnowledgeList(/*UserCache.getAccessToken(), */pageNum)
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<CaseKnowledgeExt>>() {
                    @Override
                    public void onNext(Result<CaseKnowledgeExt> result) {
                        CaseKnowledgeExt ext = result.getData();
                        if (ext == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        List<CaseKnowledgeBean> list = ext.getList();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        pageNum++;
                        formatData(list);
                        if (pageNum == result.getTotalCount()) {
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
                        isRefreshing.set(false);
                    }
                });
    }

    private synchronized void formatData(List<CaseKnowledgeBean> list) {
        if (CollectionUtil.isEmpty(list)) return;
        datas.addAll(list);
        datas.get(0).setHeader(true);
        for (int i = 0; i < datas.size(); i++) {
            CaseKnowledgeBean bean = datas.get(i);
            if (i == 0) {
                continue;
            }
            CaseKnowledgeBean lastBean = datas.get(i - 1);
            bean.setHeader(bean.getKnowledgeType() != lastBean.getKnowledgeType());
        }
    }
}

