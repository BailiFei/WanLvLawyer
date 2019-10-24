package com.lawyer.controller.video.vm;

import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.support.v4.widget.SwipeRefreshLayout;

import com.lawyer.R;
import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.video.VideoShortChildFm;
import com.lawyer.databinding.FmVideoShortChildBinding;
import com.lawyer.databinding.ItemVideoShortBinding;
import com.lawyer.entity.CaseTypeBean;
import com.lawyer.entity.CaseVideoExt;
import com.lawyer.entity.VideoBean;
import com.lawyer.mvvm.adapter.image.RoundTransform;
import com.lawyer.mvvm.adapter.recyclerview.RecyclerViewAdapter;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import ink.itwo.common.img.IMGLoad;
import ink.itwo.common.util.CollectionUtil;
import ink.itwo.net.NetManager;
import ink.itwo.net.life.RxLifecycle;
import ink.itwo.net.transformer.ResultTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 @author wang
 on 2019/2/26 */

public class VideoChildViewModel extends AbsViewModel<VideoShortChildFm, FmVideoShortChildBinding> {

    private int pageNum;
    private List<VideoBean> datas = new ArrayList<>();
    private RecyclerViewAdapter<VideoBean, ItemVideoShortBinding> adapter = new RecyclerViewAdapter<VideoBean, ItemVideoShortBinding>(R.layout.item_video_short, datas) {
        @Override
        protected void convert(ItemVideoShortBinding bind, VideoBean item) {
            super.convert(bind, item);
            bind.videoplayer.setUp(item.getId(), item.getVideoUrl(), "", Jzvd.SCREEN_WINDOW_LIST);
            IMGLoad.with(bind.videoplayer.getContext())
                    .load(item.getPicUrl())
                    .centerCrop()
                    .transform(new RoundTransform())
                    .placeholder(0)
                    .error(0)
                    .into(bind.videoplayer.thumbImageView);
        }
    };
    private CaseTypeBean typeBean;
    @Bindable
    public ObservableBoolean isRefreshing = new ObservableBoolean(false);
    @Bindable
    public SwipeRefreshLayout.OnRefreshListener refreshListener = this::onRefresh;

    public VideoChildViewModel(VideoShortChildFm videoShortChildFm, FmVideoShortChildBinding fmVideoShortChildBinding) {
        super(videoShortChildFm, fmVideoShortChildBinding);
        adapter.bindToRecyclerView(bind.recyclerView);
        adapter.setOnLoadMoreListener(() -> getInfo(), bind.recyclerView);
    }

    @Override
    protected void putSkip(int key, Object object) {
        typeBean = (CaseTypeBean) object;
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
        if (typeBean == null) return;
        NetManager.http().create(API.class)
                .caseVideoList(/*UserCache.getAccessToken(),*/ pageNum, typeBean.getId())
                .compose(RxLifecycle.bind(getView()))
                .compose(ResultTransformer.handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver<Result<CaseVideoExt>>() {
                    @Override
                    public void onNext(Result<CaseVideoExt> result) {
                        CaseVideoExt data = result.getData();
                        if (data == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        List<VideoBean> list = data.getList();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        pageNum++;
                        datas.addAll(list);
                        if (pageNum == result.getTotalCount()) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        adapter.loadMoreComplete();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        isRefreshing.set(false);
                    }
                });
    }
}
