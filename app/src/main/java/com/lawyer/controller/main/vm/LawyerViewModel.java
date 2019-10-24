package com.lawyer.controller.main.vm;

import android.databinding.Bindable;
import android.view.View;

import com.lawyer.BR;
import com.lawyer.R;
import com.lawyer.base.AbsViewModel;
import com.lawyer.controller.main.MainLawyerFm;
import com.lawyer.databinding.FmMainLawyerBinding;
import com.lawyer.databinding.ItemMainLawyerBinding;
import com.lawyer.entity.User;
import com.lawyer.entity.UserCaseBean;
import com.lawyer.entity.UserCaseBeanExt;
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
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 @author wang
 on 2019/3/5 */

public class LawyerViewModel extends AbsViewModel<MainLawyerFm, FmMainLawyerBinding> {
    private int pageNum=0, pageSize = 20;
    private int currentPosition;
    @Bindable
    public User user;
    private List<UserCaseBean> data = new ArrayList<>();
    private RecyclerViewAdapter<UserCaseBean, ItemMainLawyerBinding> adapter =
            new RecyclerViewAdapter<UserCaseBean, ItemMainLawyerBinding>(R.layout.item_main_lawyer, data) {
                @Override
                protected void convert(ItemMainLawyerBinding bind, UserCaseBean item) {
                    super.convert(bind, item);
                    bind.getRoot().setOnClickListener(v-> onSkip.put(1,item));
                }
            };
    public View.OnClickListener withdrawClick =v -> {
        onSkip.put(3, user);
    };

    public LawyerViewModel(MainLawyerFm mainLawyerFm, FmMainLawyerBinding fmMainLawyerBinding) {
        super(mainLawyerFm, fmMainLawyerBinding);
        bind.recyclerView.addItemDecoration(Divider.newBuilder()
                .put(Divider.top, DeviceUtil.getDimensionPx(R.dimen.dp_10))
                .build());

        adapter.bindToRecyclerView(bind.recyclerView);
        adapter.setOnLoadMoreListener(this::getList, bind.recyclerView);
    }

    @Override
    protected void putSkip(int key, Object object) {
        //切换 tab
        if (key == 11) {
            currentPosition = (int) object;
            onRefresh();
        }
    }
    public void getInfo() {
        onRefresh();
//        getUserInfo();
    }

    private void onRefresh() {
        data.clear();
        pageNum = 0;
        adapter.notifyDataSetChanged();
        getList();
    }


    private void getList() {
        get().compose(NetTransformer.handle(getView()))
                .subscribe(new ResultObserver<Result<List<UserCaseBeanExt>>>() {
                    @Override
                    public void onNext(Result<List<UserCaseBeanExt>> listResult) {
                        if (listResult == null) {
                            adapter.loadMoreFail();
                            return;
                        }
                        List<UserCaseBeanExt> list = listResult.getData();
                        if (CollectionUtil.isEmpty(list)) {
                            adapter.loadMoreEnd();
                            return;
                        }
                        for (UserCaseBeanExt ext : list) {
                            ext.setCurrentPosition(currentPosition);
                            ext.setId(ext.getCaseId());
                        }
                        data.addAll(list);
                        adapter.loadMoreComplete();
                        pageNum++;
                    }
                });
    }

    private Observable<Result<List<UserCaseBeanExt>>> get() {
        if (currentPosition != 0) {
            return NetManager.http().create(API.class)
                    .userMyCaseList(UserCache.getAccessToken(), pageNum, pageSize, currentPosition == 1 ? "solving" : "done");
        } else {
            return NetManager.http().create(API.class)
                    .userMyCaseApplyList(UserCache.getAccessToken(), pageNum, pageSize);
        }
    }

    public void getUserInfo() {
        NetManager.http().create(API.class)
                .userQuery(UserCache.getAccessToken())
//                .compose(NetTransformer.handle(getView()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResultObserver<Result<User>>() {
                    @Override
                    public void onNext(Result<User> result) {
                        user = result.getData();
                        notifyPropertyChanged(BR.user);
                    }
                });
    }
}
