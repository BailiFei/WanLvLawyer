package com.lawyer.dialog;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.entity.CaseTypeBean;
import com.lawyer.net.API;
import com.lawyer.net.Result;
import com.lawyer.net.ResultObserver;

import java.util.ArrayList;
import java.util.List;

import ink.itwo.common.ctrl.CommonDialog;
import ink.itwo.common.util.CollectionUtil;
import ink.itwo.common.util.DeviceUtil;
import ink.itwo.net.NetManager;
import ink.itwo.net.transformer.NetTransformer;

/**
 Created by wangtaian on 2019-05-31. */
public class CaseTypeDialog extends CommonDialog<MainActivity> {
    public static CaseTypeDialog newInstance() {

        Bundle args = new Bundle();

        CaseTypeDialog fragment = new CaseTypeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView recyclerView;
    private List<CaseTypeBean> data = new ArrayList<>();

    public interface CaseTypeDialogCallback {
        void onResult(CaseTypeBean bean);
    }

    private CaseTypeDialogCallback callback;

    public CaseTypeDialog setCallback(CaseTypeDialogCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_case_type;
    }

    @Override
    public void convertView(View view) {
        setWidth(MATCH_PARENT)
                .setHeight(DeviceUtil.getDimensionPx(R.dimen.dp_275))
                .setShowBottom(true);
        recyclerView = view.findViewById(R.id.recycler_view);
        mAdapter.bindToRecyclerView(recyclerView);
        getInfo();
    }

    private BaseQuickAdapter mAdapter = new BaseQuickAdapter<CaseTypeBean, BaseViewHolder>(R.layout.item_case_type, data) {

        @Override
        protected void convert(BaseViewHolder helper, CaseTypeBean item) {
            helper.setText(R.id.text, item.getName());
            helper.getView(R.id.text).setOnClickListener(v -> {
                if (callback != null) {
                    callback.onResult(item);
                    dismissAllowingStateLoss();
                }
            });
        }
    };

    private void getInfo() {
        NetManager.http().create(API.class)
                .caseVideoCaseType(0)
                .compose(NetTransformer.handle(this))
                .subscribe(new ResultObserver<Result<List<CaseTypeBean>>>() {
                    @Override
                    public void onNext(Result<List<CaseTypeBean>> result) {
                        List<CaseTypeBean> list = result.getData();
                        if (!CollectionUtil.isEmpty(list)) {
                            data.addAll(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                });
    }
}
