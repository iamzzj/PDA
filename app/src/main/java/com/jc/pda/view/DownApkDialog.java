package com.jc.pda.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.jc.pda.BuildConfig;
import com.jc.pda.R;
import com.jc.pda.utils.Path;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.Logger;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by z on 2018/1/16.
 */

public class DownApkDialog extends Dialog {
    @BindView(R.id.npb_down_apk_dialog_progress)
    NumberProgressBar npbDownApkDialogProgress;
    private String downLoadUrl = "";
    private String savePath ="";
    private FileDownloadListener fileDownloadListener = new MyFileDownloadListener();

    public DownApkDialog(@NonNull Context context) {
        super(context);
    }

    public DownApkDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public DownApkDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_down_apk, null);
        setContentView(view);
        ButterKnife.bind(this, view);

        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 5 / 6; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);// 点击Dialog外部消失

        FileDownloader.setup(getContext());
        if (!TextUtils.isEmpty(downLoadUrl) && !TextUtils.isEmpty(savePath)) {
            File apkFile = new File(savePath);
            if (apkFile.exists()) {
                apkFile.delete();
            }
            FileDownloader.getImpl().create(downLoadUrl)
                    .setPath(savePath)
                    .setListener(fileDownloadListener).start();
        }
    }

    public void setDownLoadUrl(String downLoadUrl,String savePath) {
        this.downLoadUrl = downLoadUrl;
        this.savePath = savePath;
    }

    private class MyFileDownloadListener extends FileDownloadListener {

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            npbDownApkDialogProgress.setProgress(soFarBytes*100/totalBytes);
        }

        @Override
        protected void completed(BaseDownloadTask task) {

            File apkFile = new File(savePath);
            if (!apkFile.exists()) {
                return;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            getContext().startActivity(intent);
            dismiss();
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {

        }

        @Override
        protected void warn(BaseDownloadTask task) {

        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
        FileDownloader.getImpl().pause(fileDownloadListener);
    }
}
