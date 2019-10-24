package com.m7.imkfsdk.chat;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.m7.imkfsdk.R;
import com.m7.imkfsdk.constant.Constants;
import com.moor.imkf.model.entity.ScheduleConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pangw on 2018/3/6.
 */

public class Scheduledialog extends DialogFragment {

    private ListView investigateListView;

    private List<ScheduleConfig.EntranceNodeBean.EntrancesBean> Schedules = new ArrayList<>();

    private ScheduleAdapter adapter;

    private String scheduleId = "";

    private String processId ="";

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("选择日程");
        getDialog().setCanceledOnTouchOutside(false);

        // Get the layout inflater
        View view = inflater.inflate(R.layout.kf_dialog_common, null);
        investigateListView = (ListView) view.findViewById(R.id.investigate_list);

        Bundle bundle = getArguments();
        Schedules = (List<ScheduleConfig.EntranceNodeBean.EntrancesBean>) bundle.getSerializable("Schedules");
        scheduleId = bundle.getString("scheduleId");
        processId = bundle.getString("processId");
        adapter = new ScheduleAdapter(getActivity(), Schedules);

        investigateListView.setAdapter(adapter);

        investigateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                ScheduleConfig.EntranceNodeBean.EntrancesBean bean = (ScheduleConfig.EntranceNodeBean.EntrancesBean) parent.getAdapter().getItem(position);
                ChatActivity.startActivity(getActivity(), Constants.TYPE_SCHEDULE,scheduleId,processId,bean.getProcessTo(),bean.getProcessType(),bean.get_id());

            }
        });

        return view;
    }



    @Override
    public void show(android.app.FragmentManager manager, String tag) {
        if (!this.isAdded()) {
            try {
                super.show(manager, tag);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
        }

    }
}
