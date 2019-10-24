package com.m7.imkfsdk;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.moor.imkf.requesturl.RequestUrl;

/**
 * Created by pangw on 2018/8/21.
 */

public class SettingActivity extends Activity {
    private RadioGroup mRadioGroup;
    private int type = 0;
    private EditText accessId_edt;
    private Button commit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mRadioGroup = findViewById(R.id.setting_rg);
        accessId_edt = findViewById(R.id.setting_accessid);
        commit = findViewById(R.id.setting_commit);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case 1://研发
                        type = 1;
                        break;
                    case 2://测试
                        type = 2;
                        break;
                    case 3://正式
                        type = 3;
                        break;
                    case 4://自定义
                        type = 4;
                        break;
                }
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!accessId_edt.getText().toString().trim().equals("")) {

                    switch (type) {
                        case 1://研发
                            RequestUrl.type = 1;
                            break;
                        case 2://测试
                            RequestUrl.type = 2;
                            break;
                        case 3://正式
                            RequestUrl.type = 3;
                            break;
                        case 4://自定义

                            break;
                    }

                   // MainActivity.accessId = accessId_edt.getText().toString().trim();
                    Toast.makeText(SettingActivity.this, "设定成功", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(SettingActivity.this, "请输入accessId", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
