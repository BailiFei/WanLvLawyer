package com.lawyer.controller.main.vm;

import android.databinding.Bindable;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.main.MainBiddingFm;
import com.lawyer.databinding.FmMainBiddingBinding;
import com.lawyer.databinding.ItemMainBiddingBinding;
import com.lawyer.entity.CityBean;
import com.lawyer.entity.LawyerIndexBean;
import com.lawyer.entity.UserCaseBean;
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
import ink.itwo.net.transformer.ResultTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 @author wang
 on 2019/2/22 */

public class BiddingViewModel extends AbsViewModel<MainBiddingFm, FmMainBiddingBinding> {
    private int pageNum, pageSize = 20;
    private List<UserCaseBean> data = new ArrayList<>();
    private RecyclerViewAdapter<UserCaseBean, ItemMainBiddingBinding> adapter =
            new RecyclerViewAdapter<UserCaseBean, ItemMainBiddingBinding>(R.layout.item_main_bidding, data){
                @Override
                protected void convert(ItemMainBiddingBinding bind, UserCaseBean item) {
                    super.convert(bind, item);
                    bind.getRoot().setOnClickListener(v->onSkip.put(1,item));
                }
            };
    @Bindable
    public LawyerIndexBean indexBean;
    @Bindable
    public String cityName;
    public BiddingViewModel(MainBiddingFm mainBiddingFm, FmMainBiddingBinding fmMainBiddingBinding) {
        super(mainBiddingFm, fmMainBiddingBinding);
        bind.recyclerView.addItemDecoration(Divider.newBuilder()
                .put(Divider.top, DeviceUtil.getDimensionPx(R.dimen.dp_20))
                .build());
        adapter.bindToRecyclerView(bind.recyclerView);
        adapter.setOnLoadMoreListener(this::getInfo, bind.recyclerView);
    }
    public void  onRefresh() {
        pageNum = 0;
        data.clear();
        adapter.notifyDataSetChanged();
        getInfo();
    }

    public void getInfo() {
        NetManager.http().create(API.class)
                .lawyerIndex(UserCache.getAccessToken(), pageNum, pageSize)
                .compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<LawyerIndexBean>>() {
                    @Override
                    public void onNext(Result<LawyerIndexBean> result) {
                         indexBean = result.getData();
                         notifyPropertyChanged(BR.indexBean);
                        if (indexBean == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        List<UserCaseBean> list = indexBean.getList();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        data.addAll(list);
                        pageNum++;
                        adapter.loadMoreComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        adapter.loadMoreFail();
                    }
                });
        userFetchCity();
    }

    private void userFetchCity() {
        NetManager.http().create(API.class)
                .userFetchCity()
                .compose(ResultTransformer.handleResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver<Result<CityBean>>() {
                    @Override
                    public void onNext(Result<CityBean> result) {
                        CityBean data = result.getData();
                        if (data != null) {
                            cityName = data.getCity();
                            notifyPropertyChanged(BR.cityName);
                        }
                    }

                });
    }

}
