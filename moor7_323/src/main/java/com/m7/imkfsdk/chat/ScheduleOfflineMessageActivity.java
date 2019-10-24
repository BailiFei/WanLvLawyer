package com.m7.imkfsdk.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.m7.imkfsdk.R;
import com.moor.imkf.IMChatManager;
import com.moor.imkf.OnLeaveMsgConfigListener;
import com.moor.imkf.OnSubmitOfflineMessageListener;
import com.moor.imkf.model.entity.LeaveMsgField;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pangw on 2018/3/7.
 */

public class ScheduleOfflineMessageActivity extends Activity {
    EditText id_et_content;
    Button btn_submit;
    TextView back;
    private String ToPeer;
    private String LeavemsgNodeId;
    LoadingFragmentDialog loadingFragmentDialog;

    private LinearLayout offline_ll_custom_field;
    List<LeaveMsgField> lmfList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kf_dialog_offline);

        loadingFragmentDialog = new LoadingFragmentDialog();

        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        id_et_content = (EditText) findViewById(R.id.id_et_content);

        btn_submit = (Button) findViewById(R.id.id_btn_submit);
        offline_ll_custom_field = (LinearLayout) findViewById(R.id.offline_ll_custom_field);

        Intent intent = getIntent();
        ToPeer = intent.getStringExtra("ToPeer");
        LeavemsgNodeId = intent.getStringExtra("LeavemsgNodeId");

        IMChatManager.getInstance().getScheduleLeaveMsgConfig(id_et_content,LeavemsgNodeId, new OnLeaveMsgConfigListener() {
            @Override
            public void onSuccess(List<LeaveMsgField> fieldList) {
                if (fieldList != null && fieldList.size() > 0) {
                    lmfList = fieldList;
                    for (int i = 0; i < fieldList.size(); i++) {
                        LeaveMsgField leaveMsgField = fieldList.get(i);
                        if (leaveMsgField.enable) {
                            RelativeLayout singleView = (RelativeLayout) LayoutInflater.from(ScheduleOfflineMessageActivity.this).inflate(R.layout.kf_offline_edittext, offline_ll_custom_field, false);
                            TextView erp_field_single_tv_name = (TextView) singleView.findViewById(R.id.erp_field_data_tv_name);
                            erp_field_single_tv_name.setText(leaveMsgField.name);
                            erp_field_single_tv_name.setTag(leaveMsgField.required);
                            EditText erp_field_single_et_value = (EditText) singleView.findViewById(R.id.erp_field_data_et_value);
                            erp_field_single_et_value.setTag(leaveMsgField._id);
                            offline_ll_custom_field.addView(singleView);
                        }
                    }
                } else {

                }
            }

            @Override
            public void onFailed() {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = id_et_content.getText().toString().trim();
                int childSize = offline_ll_custom_field.getChildCount();
                HashMap<String, String> datas = new HashMap<>();
                for (int i = 0; i < childSize; i++) {
                    RelativeLayout childView = (RelativeLayout) offline_ll_custom_field.getChildAt(i);
                    EditText et = (EditText) childView.getChildAt(1);
                    String id = (String) et.getTag();
                    String value = et.getText().toString().trim();
                    TextView tv_single_required = (TextView) childView.getChildAt(0);
                    String fieldName = tv_single_required.getText().toString();
                    Boolean required = (Boolean) tv_single_required.getTag();
                    if (required) {
                        if ("".equals(value)) {
                            Toast.makeText(ScheduleOfflineMessageActivity.this, fieldName + "是必填项", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    datas.put(id, value);

                }

                if (!"".equals(content)) {
                    loadingFragmentDialog.show(getFragmentManager(), "");
                    IMChatManager.getInstance().submitOfflineMessage(ToPeer, content, datas, lmfList, new OnSubmitOfflineMessageListener() {
                        @Override
                        public void onSuccess() {
                            loadingFragmentDialog.dismiss();
                            Toast.makeText(ScheduleOfflineMessageActivity.this, "提交留言成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailed() {
                            loadingFragmentDialog.dismiss();
                            Toast.makeText(ScheduleOfflineMessageActivity.this, "提交留言失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                } else {
                    Toast.makeText(ScheduleOfflineMessageActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra("ToPeer") != null) {
            ToPeer = intent.getStringExtra("ToPeer");
        }
    }

    @Override
    protected void onDestroy() {
        IMChatManager.getInstance().quitSDk();
        super.onDestroy();
    }
}
