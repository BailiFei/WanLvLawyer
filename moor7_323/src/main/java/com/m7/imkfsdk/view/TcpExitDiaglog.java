package com.m7.imkfsdk.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.m7.imkfsdk.R;


/**
 * <p>
 * Package Name:com.dustess.activityalbum.widgets
 * </p>
 * <p>
 * Class Name:CommonHasButtonDiaglog
 * <p>
 * Description: 公共的带有button的dialog（标题可有可无，按钮可一个可两个）
 * </p>
 *
 * @Author 张小五先森
 * @Version 1.0 2018/7/25 Release
 * @Reviser: Martin
 * @Modification Time:2018/10/17 下午4:02
 */
public class TcpExitDiaglog extends Dialog {
    public TcpExitDiaglog(@NonNull Context context) {
        super(context);
    }

    public TcpExitDiaglog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }


    public static class Builder {
        private String message;
        private String messageDetail;
        private View contentView;
        private String positiveButtonText;
        private int positiveColor;
        private int negativeColor;
        private int textColor;
        private String negativeButtonText;
        private String singleButtonText;
        private View.OnClickListener positiveButtonClickListener;
        private View.OnClickListener negativeButtonClickListener;
        private View.OnClickListener singleButtonClickListener;

        private View layout;
        private TcpExitDiaglog dialog;

        public Builder(Context context) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            dialog = new TcpExitDiaglog(context, R.style.commonDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.tcp_exit_dialog, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessageDetail(String message) {
            this.messageDetail = message;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, int positiveColor, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveColor = positiveColor;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, int negativeColor, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeColor = negativeColor;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setSingleButton(String singleButtonText, int textColor, View.OnClickListener listener) {
            this.singleButtonText = singleButtonText;
            this.textColor = textColor;
            this.singleButtonClickListener = listener;
            return this;
        }

        /**
         * 创建单按钮对话框
         *
         * @return
         */
        public TcpExitDiaglog createSingleButtonDialog() {
            showSingleButton();
            layout.findViewById(R.id.singleButton).setOnClickListener(singleButtonClickListener);
            //如果传入的按钮文字为空，则使用默认的“返回”
            if (singleButtonText != null) {
                ((TextView) layout.findViewById(R.id.singleButton)).setText(singleButtonText);
            } else {
                ((TextView) layout.findViewById(R.id.singleButton)).setText("返回");
            }
            ((TextView) layout.findViewById(R.id.singleButton)).setTextColor(textColor);
            create(false);
            return dialog;
        }

        /**
         * 创建双按钮对话框
         *
         * @return
         */
        public TcpExitDiaglog createTwoButtonDialog() {
            showTwoButton();
            layout.findViewById(R.id.positiveButton).setOnClickListener(positiveButtonClickListener);
            layout.findViewById(R.id.negativeButton).setOnClickListener(negativeButtonClickListener);
            //如果传入的按钮文字为空，则使用默认的“是”和“否”
            ((TextView) layout.findViewById(R.id.positiveButton)).setTextColor(positiveColor);
            //0xffff00ff是int类型的数据，分组一下0x|ff|ff00ff，0x是代表颜色整数的标记，ff是表示透明度，ff00ff表示颜色，注意：这里ffff00ff必须是8个的颜色表示，不接受ff00ff这种6个的颜色表示。
            ((TextView) layout.findViewById(R.id.negativeButton)).setTextColor(negativeColor);
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
            } else {
                ((TextView) layout.findViewById(R.id.positiveButton)).setText("确定");
            }
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
            } else {
                ((TextView) layout.findViewById(R.id.negativeButton)).setText("取消");
            }
            create(true);
            return dialog;
        }

        /**
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        private void create(boolean BackBtIsUse) {
            if (message != null) {      //设置提示内容
                ((TextView) layout.findViewById(R.id.tv_title)).setText(message);
            }
            if (!TextUtils.isEmpty(messageDetail)) {
                ((TextView) layout.findViewById(R.id.message_content)).setText(messageDetail);
            }
            dialog.setContentView(layout);
            dialog.setCancelable(BackBtIsUse);     //用户可以点击手机Back键取消对话框显示
            dialog.setCanceledOnTouchOutside(false);//用户不能通过点击对话框之外的地方取消对话框显示
        }

        /**
         * 显示双按钮布局，隐藏单按钮
         */
        private void showTwoButton() {
            layout.findViewById(R.id.singleButtonLayout).setVisibility(View.GONE);
            layout.findViewById(R.id.twoButtonLayout).setVisibility(View.VISIBLE);
        }

        /**
         * 显示单按钮布局，隐藏双按钮
         */
        private void showSingleButton() {
            layout.findViewById(R.id.singleButtonLayout).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.twoButtonLayout).setVisibility(View.GONE);
        }

    }
}
