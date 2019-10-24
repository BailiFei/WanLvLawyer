package com.lawyer.controller.main.head;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lawyer.R;
import com.lawyer.databinding.ItemAdvisoryCaseTypeBinding;
import com.lawyer.entity.CaseTypeBean;
import com.lawyer.mvvm.adapter.listview.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 @author wang
 on 2019/2/25 */

public class CasesHeadView extends LinearLayout {
    private GridView gridView;
    private TextView tvCaseType;
    private ListViewAdapter adapter;
    private List<CaseTypeBean> datas = new ArrayList<>();
    private CasesHeadViewListener listener;

    public CasesHeadView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_advisory_case_head, this, true);
        gridView = findViewById(R.id.grid_view_case_type);
        tvCaseType = findViewById(R.id.tv_case_type);
        adapter = new ListViewAdapter<CaseTypeBean, ItemAdvisoryCaseTypeBinding>(datas, R.layout.item_advisory_case_type, 0) {
            @Override
            public void convert(ItemAdvisoryCaseTypeBinding bind, CaseTypeBean item) {
                bind.layout.setOnClickListener(v -> {
                    tvCaseType.setText(item.getName());
                    if (listener != null) listener.onClick(item);
                });
            }
        };
        tvCaseType.setText(R.string.case_classical);
        gridView.setAdapter(adapter);
    }

    public void setData(List<CaseTypeBean> data) {
        datas.clear();
        if (data != null) {
            datas.addAll(data);
        }
        adapter.notifyDataSetChanged();

    }

    public void setListener(CasesHeadViewListener listener) {
        this.listener = listener;
    }

    public interface CasesHeadViewListener {
        void onClick(CaseTypeBean item);
    }


}
