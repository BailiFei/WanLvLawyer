package com.lawyer.dialog;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.databinding.DgAlertBinding;

import ink.itwo.common.ctrl.ObserveDialog;
import ink.itwo.common.util.DeviceUtil;

/**
 @author wang
 on 2019/3/4 */

public class RxAlertDialog extends ObserveDialog<Boolean, MainActivity> implements View.OnClickListener {
    public static RxAlertDialog newInstance(Builder builder) {

        Bundle args = new Bundle();

        RxAlertDialog fragment = new RxAlertDialog();
        args.putParcelable("builder", builder);
        fragment.setArguments(args);
        return fragment;
    }

    private DgAlertBinding bind;
    private Builder builder;

    @Override
    public int intLayoutId() {
        return R.layout.dg_alert;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater, R.layout.dg_alert, container, false);
        View view = bind.getRoot();
        convertView(view);
        return view;
    }

    @Override
    public void convertView(View view) {
         builder = getArguments().getParcelable("builder");
        setWidth(DeviceUtil.getDimensionPx(R.dimen.dp_280))
                .setHeight(WRAP_CONTENT)
                .setOutCancel(builder.outCancel);
        if (!TextUtils.isEmpty(builder.titleStr)) {
            bind.tvTitle.setText(builder.titleStr);
        }
        if (!TextUtils.isEmpty(builder.desStr)) {
            bind.tvDes.setText(builder.desStr);
        }
        bind.tvCommit.setText(builder.commitStr);
        bind.tvEsc.setText(builder.escStr);
        bind.tvCommit.setOnClickListener(this);
        boolean isTitleSingle = !TextUtils.isEmpty(builder.titleSingleStr);
        bind.tvTitleSingle.setVisibility(isTitleSingle ? View.VISIBLE : View.GONE);
        bind.tvDes.setVisibility(isTitleSingle ? View.INVISIBLE : View.VISIBLE);
        bind.tvTitle.setVisibility(isTitleSingle ? View.INVISIBLE : View.VISIBLE);
        if (isTitleSingle) bind.tvTitleSingle.setText(builder.titleSingleStr);
        if (builder.outCancel) {
            bind.tvEsc.setOnClickListener(this);
            bind.tvEsc.setVisibility(View.VISIBLE);
        } else {
            bind.tvEsc.setVisibility(View.GONE);
        }
    }

    public static final class Builder implements Parcelable {
        private boolean outCancel = true;
        private String titleStr;
        private String desStr;
        private String commitStr = "确认";
        private String escStr = "取消";
        private boolean commitCancel = false;
        private String titleSingleStr;
        private Builder() {
        }

        public Builder titleSingleStr(String titleSingleStr) {
            this.titleSingleStr = titleSingleStr;
            return this;
        }

        public Builder commitCancel(boolean commitCancel) {
            this.commitCancel = commitCancel;
            return this;
        }

        public Builder outCancel(boolean outCancel) {
            this.outCancel = outCancel;
            return this;
        }

        public Builder titleStr(String titleStr) {
            this.titleStr = titleStr;
            return this;
        }

        public Builder desStr(String desStr) {
            this.desStr = desStr;
            return this;
        }

        public Builder commitStr(String commitStr) {
            this.commitStr = commitStr;
            return this;
        }

        public Builder escStr(String escStr) {
            this.escStr = escStr;
            return this;
        }

        public RxAlertDialog build() {
            return RxAlertDialog.newInstance(this);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.outCancel ? (byte) 1 : (byte) 0);
            dest.writeString(this.titleStr);
            dest.writeString(this.desStr);
            dest.writeString(this.commitStr);
            dest.writeString(this.escStr);
            dest.writeByte(this.commitCancel ? (byte) 1 : (byte) 0);
            dest.writeString(this.titleSingleStr);
        }

        protected Builder(Parcel in) {
            this.outCancel = in.readByte() != 0;
            this.titleStr = in.readString();
            this.desStr = in.readString();
            this.commitStr = in.readString();
            this.escStr = in.readString();
            this.commitCancel = in.readByte() != 0;
            this.titleSingleStr = in.readString();
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public Builder createFromParcel(Parcel source) {
                return new Builder(source);
            }

            @Override
            public Builder[] newArray(int size) {
                return new Builder[size];
            }
        };
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void onClick(View v) {
        if (dialogEmitter != null) {
            dialogEmitter.onNext(v.getId() == R.id.tv_commit);
        }
        if (v.getId() == R.id.tv_esc) {
            dismiss();
        }
        if (v.getId() == R.id.tv_commit&&builder.commitCancel) {
            dismiss();
        }
    }
}
