package com.lawyer.dialog;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lawyer.MainActivity;
import com.lawyer.R;
import com.lawyer.databinding.DgDownloadProgressBinding;

import ink.itwo.common.ctrl.ObserveDialog;
import ink.itwo.common.util.DeviceUtil;
import ink.itwo.net.file.DownloadProgressListener;

/**
 @author wang
 on 2019/3/4 */

public class RxDownloadProgressDialog extends ObserveDialog<Boolean, MainActivity> implements DownloadProgressListener {
    private DgDownloadProgressBinding binding;

    @Override
    public int intLayoutId() {
        return R.layout.dg_download_progress;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false);
        View view = binding.getRoot();
        convertView(view);
        return view;
    }

    @Override
    public void convertView(View view) {
        setWidth(DeviceUtil.getDimensionPx(R.dimen.dp_280))
                .setHeight(DeviceUtil.getDimensionPx(R.dimen.dp_100))
                .setOutCancel(false);
    }

    @Override
    public void update(long bytesRead, long contentLength, boolean done) {
        if (binding.seekBar != null) {
            int p = contentLength == 0 ? 0 : (int) (bytesRead * 100 / contentLength);
            binding.seekBar.setProgress(p);
        }
        if (dialogEmitter != null && done) {
            dialogEmitter.onNext(true);
            dismiss();
        }
    }
}
