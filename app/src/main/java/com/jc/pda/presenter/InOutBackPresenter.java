package com.jc.pda.presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.jc.pda.BuildConfig;
import com.jc.pda.entitynet.WlCodeCheckNet;
import com.jc.pda.presenter.view.InOutBackView;
import com.jc.pda.utils.Global;
import com.jc.pda.utils.JsonUtils;
import com.jc.pda.utils.PermissionUtils;
import com.jc.pda.web.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by z on 2018/1/8.
 */

public class InOutBackPresenter {
    private InOutBackView inOutBackView;
    private Context context;

    public InOutBackPresenter(Context context, InOutBackView inOutBackView) {
        this.context = context;
        this.inOutBackView = inOutBackView;
    }

    public void wlCodeCheck(final String Codes) {
        inOutBackView.dialogShow("正在验证条码");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Object[] strs = {
                        "CodeType", inOutBackView.getCodeType(),
                        "Codes", Codes
                };
                String result = WebService.sendWebService("WlCodeCheck", strs);
                e.onNext(result);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String string) {
                        inOutBackView.dialogDismiss();
                        try {
                            JSONObject result = new JSONObject(string);
                            if (result.getBoolean("Success")) {
                                inOutBackView.setWlCodeCheckSuccess();
                            } else {
                                List<WlCodeCheckNet> wlCodeNets = JsonUtils.string2ObjectArray(result.getString("Data"), WlCodeCheckNet.class);
                                inOutBackView.setWlCodeCheckFail(wlCodeNets);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        inOutBackView.err("验证出现错误\n" + e.getMessage());
                        inOutBackView.dialogDismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void installIscan() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                String pdaPath = context.getCacheDir().getAbsolutePath() + "/" + Global.NAME + "pda";
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (PermissionUtils.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            pdaPath = Environment.getExternalStorageDirectory().getPath() + "/" + Global.NAME + "pda";
                        }
                    } else {
                        pdaPath = Environment.getExternalStorageDirectory().getPath() + "/" + Global.NAME + "pda";
                    }
                }
                InputStream is = context.getAssets().open(
                        "iScan4.2.1.apk");
                File apkFile = new File(pdaPath,
                        "iScan.apk");
                if (!apkFile.getParentFile().exists()) {
                    apkFile.getParentFile().mkdirs();
                } else {
                    apkFile.delete();
                }
                apkFile.createNewFile();

                FileOutputStream fos = new FileOutputStream(apkFile);
                // 缓存
                byte buf[] = new byte[1024];
                // 写入到文件中
                int i = 0;
                while ((i = is.read(buf)) > 0) {
                    fos.write(buf, 0, i);
                }
                fos.close();
                is.close();

                e.onNext("");
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        String pdaPath = context.getCacheDir().getAbsolutePath() + "/" + Global.NAME + "pda";
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (PermissionUtils.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    pdaPath = Environment.getExternalStorageDirectory().getPath() + "/" + Global.NAME + "pda";
                                }
                            } else {
                                pdaPath = Environment.getExternalStorageDirectory().getPath() + "/" + Global.NAME + "pda";
                            }
                        }
                        File apkFile = new File(pdaPath,
                                "iScan.apk");
                        if (!apkFile.exists()) {
                            return;
                        }

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        //判断是否是AndroidN以及更高的版本
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
                            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                        } else {
                            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void havePackge() {
        PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase("com.android.auto.iscan"))
                return;
        }

        inOutBackView.installIscan();
    }
}
