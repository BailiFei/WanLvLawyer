package com.lawyer.dialog;

import android.os.Bundle;
import android.support.design.widget.ExtendTabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.entity.DistrictBean;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.ctrl.CommonDialog;
import ink.itwo.common.util.CollectionUtil;
import ink.itwo.common.util.DeviceUtil;
import ink.itwo.common.util.ILog;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 Created by wangtaian on 2019-05-31. */
public class CityPickerDialog extends CommonDialog<MainActivity> {

    public static CityPickerDialog newInstance() {

        Bundle args = new Bundle();

        CityPickerDialog fragment = new CityPickerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private ExtendTabLayout tabLayout;
    private RecyclerView recyclerView;
    private List<DistrictBean> data = new ArrayList<>();
    private int province = 1, city = 2;
    private int type;
    private DistrictBean provinceBean;
    private CityPickerDialogCallback callback;

    public CityPickerDialog setCallback(CityPickerDialogCallback callback) {
        this.callback = callback;
        return this;
    }

    public interface CityPickerDialogCallback {
        void onResult(DistrictBean province, DistrictBean city);
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_city_picker;
    }

    @Override
    public void convertView(View view) {
        setWidth(MATCH_PARENT)
                .setHeight(DeviceUtil.getDimensionPx(R.dimen.dp_500))
                .setShowBottom(true);
        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter.bindToRecyclerView(recyclerView);
        tabLayout = view.findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("请选择"), 0);
        tabLayout.addOnTabSelectedListener(new ExtendTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(ExtendTabLayout.Tab tab) {
                ILog.d("");
                if (tab.getPosition() == 0) {
                    data.clear();
                    tabLayout.removeTabAt(1);
                    mAdapter.notifyDataSetChanged();
                    type = province;
                    tab.setText("请选择");
                    getCity(0);
                }
            }

            @Override
            public void onTabUnselected(ExtendTabLayout.Tab tab) {
                ILog.d("");
                ILog.d("");
            }

            @Override
            public void onTabReselected(ExtendTabLayout.Tab tab) {
                ILog.d("");
            }
        });


        getCity(0);
    }

    private BaseQuickAdapter mAdapter = new BaseQuickAdapter<DistrictBean, BaseViewHolder>(R.layout.item_city_picket, data) {

        @Override
        protected void convert(BaseViewHolder helper, DistrictBean item) {
            TextView textView = helper.getView(R.id.text);
            textView.setText(item.getName());
            textView.setOnClickListener(v -> {
                if (type >= city) {
                    if (callback != null) callback.onResult(provinceBean, item);
                    dismissAllowingStateLoss();
                    return;
                }

                provinceBean = item;
                type = city;
                ExtendTabLayout.Tab at = tabLayout.getTabAt(0);
                if (at != null) {
                    at.setText(item.getName());
                }
                ExtendTabLayout.Tab newTab = tabLayout.newTab().setText("请选择");
                tabLayout.addTab(newTab);
                try {
                    Field field = newTab.getClass().getDeclaredField("mView");
                    field.setAccessible(true);
                    View view =(View) field.get(newTab);
                    view.performClick();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                getCity(item.getId());
            });
        }
    };


    private void getCity(int parentId) {
        NetManager.http().create(API.class)
                .district(parentId)
                .compose(NetTransformer.handle(this))
                .subscribe(new ResultObserver<Result<List<DistrictBean>>>() {
                    @Override
                    public void onNext(Result<List<DistrictBean>> result) {
                        List<DistrictBean> list = result.getData();
                        if (!CollectionUtil.isEmpty(list)) {
                            data.clear();
                            data.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }
}
