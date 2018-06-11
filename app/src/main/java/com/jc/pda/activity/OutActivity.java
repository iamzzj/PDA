package com.jc.pda.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.barcodescandemo.ScannerInerface;
import com.bigkoo.pickerview.OptionsPickerView;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.jc.pda.R;
import com.jc.pda.adapter.CodeAdapter;
import com.jc.pda.component.DaggerOutActivityComponent;
import com.jc.pda.database.helper.CodeHelper;
import com.jc.pda.database.helper.DealerHelper;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;
import com.jc.pda.entity.Dealer;
import com.jc.pda.entity.UpDate;
import com.jc.pda.entitynet.WlCodeCheckNet;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.OutActivityModule;
import com.jc.pda.presenter.OutActivityPresenter;
import com.jc.pda.presenter.view.OutActivityView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.PermissionUtils;
import com.jc.pda.utils.SharedPreUtil;
import com.jc.pda.utils.TextUtil;
import com.jc.pda.utils.TimeUtils;
import com.jc.pda.view.HorizontalDividerItemDecoration;
import com.jc.pda.view.InputDialog;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class OutActivity extends AppCompatActivity implements OutActivityView, CodeAdapter.RightOnItemClickListener {
    public static final int SCAN_CODE_REQUEST = 10000;
    private static final int PERMISION_CAMERA = 10001;
    @BindView(R.id.v_ac_out_bar)
    View vAcOutBar;
    @BindView(R.id.tv_ac_out_dealer)
    TextView tvDealer;
    @BindView(R.id.rv_ac_out_codes)
    RecyclerView rvCodes;
    @BindView(R.id.tv_ac_out_count)
    TextView tvCount;
    @BindView(R.id.tv_ac_out_up)
    TextView tvUp;
    @BindView(R.id.tv_ac_out_keyword)
    TextView tvKeyword;
    @BindView(R.id.et_ac_out_dealerid)
    EditText etDealerid;

    private ImmersionBar immersionBar;

    @Inject
    OutActivityPresenter presenter;

    SweetAlertDialog dialog;

    //选项
    private OptionsPickerView optionDealers;
    private List<String> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();

    private List<Dealer> dealers = new ArrayList<>();
    private String dealerId;
    /**
     * 筛选关键字
     */
    private String dealersKeyword;

    //条码
    private List<Code> list;
    private CodeAdapter adapter;
    private ScannerInerface scannerInerface = new ScannerInerface(this);
    private CodeBroadcastReceiver codeReceiver = new CodeBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);
        ButterKnife.bind(this);

        DaggerOutActivityComponent
                .builder()
                .contextModule(new ContextModule(this))
                .outActivityModule(new OutActivityModule(this))
                .build()
                .inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            vAcOutBar.setVisibility(View.VISIBLE);
        }
        immersionBar = ImmersionBar.with(this)
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .flymeOSStatusBarFontColor(R.color.black)  //修改flyme OS状态栏字体颜色
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                .setOnKeyboardListener(new OnKeyboardListener() {    //软键盘监听回调
                    @Override
                    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                        //isPopup为true，软键盘弹出，为false，软键盘关闭
                    }
                });
        immersionBar.init();  //必须调用方可沉浸式

        optionDealers = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                if (options1 < options1Items.size() && options1 >= 0)
                    tvDealer.setText(options1Items.get(options1));

                if (options1 < dealers.size() && options1 >= 0 && options2 >= 0){
                    dealerId = dealers.get(options1).getDealerNo();
                    etDealerid.setText(dealerId);
                }

                Logger.i(dealerId);
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("")//标题
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(this, R.color.app_main))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(this, R.color.app_main))//取消按钮文字颜色
                .setLabels("", "", "")//设置选择的三级单位
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .isDialog(false)
                .setSelectOptions(0, 0, 0)  //设置默认选中项
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();

        rvCodes.setLayoutManager(new LinearLayoutManager(this));
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(ContextCompat.getColor(this, R.color.divider));
        rvCodes.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).build());
        //rvProductProducts.setNestedScrollingEnabled(false);

        list = new ArrayList<>();
        adapter = new CodeAdapter(this, list);
        adapter.setRightOnItemClickListener(this);
        rvCodes.setAdapter(adapter);

        // IDATA 广播接收条码
        scannerInerface.open();
        scannerInerface.enablePlayBeep(true);
        scannerInerface.setOutputMode(1);//使用广播模式0为模拟输出
        scannerInerface.lockScanKey();
        scannerInerface.enablePlayVibrate(true);

        IntentFilter mFilter = new IntentFilter("android.intent.action.SCANRESULT");
        registerReceiver(codeReceiver, mFilter);

        dealersKeyword = SharedPreUtil.getOutDealerKeyword(this);
        if (TextUtils.isEmpty(dealersKeyword)) {
            tvKeyword.setText("");
        } else {
            tvKeyword.setText(dealersKeyword);
        }
        if (presenter != null) {
            presenter.getDealers();
            presenter.havePackge();
        }
    }

    @Override
    public void setDealers(List<String> ops, List<Dealer> dealers) {
        if (dealers != null) {
            options1Items = ops;
            if (optionDealers != null) {
                optionDealers.setPicker(options1Items);
            }
            this.dealers = dealers;
        }
    }

    @Override
    public String dealersKeyword() {
        return dealersKeyword;
    }

    @OnClick(R.id.ll_ac_out_choose)
    public void choose() {
        if (list != null && list.size() > 0) {
            err("请先保存或上传订单");
        } else {
            if (optionDealers != null) {
                if (options1Items != null && options1Items.size() > 0) {
                    optionDealers.show();
                } else {
                    err("该筛选条件没有经销商");
                }
            }
        }
    }

    @OnClick(R.id.iv_ac_out_input)
    public void input() {
        final InputDialog inputDialog = new InputDialog(this);
        inputDialog.setTitle("物流码");
        inputDialog.setMaterialEditText("请输入物流码！");
        inputDialog.setOnclickListeners(new InputDialog.OnClickListeners() {
            @Override
            public void sure() {
                addCode(inputDialog.getContent());
            }

            @Override
            public void cancel() {

            }
        });
        inputDialog.show();
    }

    @OnClick(R.id.tv_ac_out_up)
    public void up() {
        if (list == null || list.size() == 0) {
            Toasty.error(this, "列表中没有物流码，请扫码后再试！", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(dealerId)) {
            Toasty.error(this, "请先选择经销商！！！", Toast.LENGTH_LONG).show();
            return;
        }

        StringBuilder sBuilder = new StringBuilder();
        for (Code code : list) {
            if (!TextUtils.isEmpty(code.getCode())) {
                sBuilder.append(code.getCode());
                sBuilder.append("|");
            }
        }

        String Codes = sBuilder.toString();
        if (Codes != null && Codes.length() > 1) {
            Codes = Codes.substring(0, Codes.length() - 1);
        }

        tvUp.setTextColor(ContextCompat.getColor(this, R.color.divider));
        tvUp.setEnabled(false);
        presenter.wlCodeCheck(Codes);
    }

    private Bill createBill(boolean isY) {
        //本地生成一个订单
        UpDate upDate = new UpDate();
        Bill bill = new Bill();
        bill.setBillId(TimeUtils.createBillNo(TimeUtils.BillTye.CHUKU, true, isY));
        bill.setProductId("");
        bill.setBatchId("");
        bill.setWholesaleId("");
        bill.setDealerId(dealerId);
        bill.setShopId("");
        bill.setBillStyle(Constant.OUT);
        bill.setUpType(Constant.FISTUP);
        bill.setUp(false);
        bill.setOldBillNo("");
        bill.setAdminId(SharedPreUtil.getLoginUser(this));
        bill.setUpDateString(upDate.getUpDate());
        bill.setUpDateInt(upDate.getTime());
        bill.setDelete(false);

        return bill;
    }

    private void addCode(String codeStr) {
        if (TextUtils.isEmpty(dealerId)) {
            Toasty.error(this, "请先选择经销商！！！", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(codeStr)) {
            Toasty.error(this, "物流码不能为空！！！", Toast.LENGTH_LONG).show();
            return;
        }
        if (haveCodeInList(codeStr)) {
            Toasty.error(this, "列表中已有该物流码", Toast.LENGTH_LONG).show();
            return;
        }
        if (CodeHelper.getSingleton(this).haveCode(codeStr, Constant.OUT)) {
            Toasty.error(this, "以前的订单中已有该物流码", Toast.LENGTH_LONG).show();
            return;
        }
        UpDate upDate = new UpDate();
        Code code = new Code();
        code.setCode(codeStr);
        code.setProductId("");
        code.setBatchId("");
        code.setWholesaleId("");
        code.setDealerId(dealerId);
        code.setShopId("");
        code.setCodeStyle(Constant.OUT);
        code.setUpType(Constant.REUP);
        code.setUpDateInt(upDate.getTime());
        code.setUpDateString(upDate.getUpDate());
        code.setDelete(false);

        list.add(code);
        adapter.notifyDataSetChanged();
        count();
    }

    private boolean haveCodeInList(String codeStr) {
        for (Code code : list) {
            if (!TextUtils.isEmpty(codeStr)) {
                if (codeStr.equals(code.getCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void dialogShow(String title) {
        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(ContextCompat.getColor(this, R.color.app_main));
        dialog.setTitleText(title);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void dialogDismiss() {
        if (dialog != null)
            dialog.dismiss();

        tvUp.setTextColor(ContextCompat.getColor(this, R.color.title_back));
        tvUp.setEnabled(true);
    }

    @Override
    public String getCodeValue() {
        return SharedPreUtil.getUserCID(this);
    }

    @Override
    public String getCodeType() {
        return "02";
    }

    @Override
    public void setWlCodeCheckSuccess() {
        if (presenter != null) {
            presenter.upData(createBill(true), list);
        }
    }

    @Override
    public void setWlCodeCheckFail(final List<WlCodeCheckNet> wlCodeCheckNets) {
        if (wlCodeCheckNets != null && wlCodeCheckNets.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("发现与服务器订单重复码\n是否移除下列重复码并提交？\n\n");
            for (int i = 0; i < wlCodeCheckNets.size(); i++) {
                stringBuilder.append(wlCodeCheckNets.get(i).getCode());
                if (i < wlCodeCheckNets.size() - 1) {
                    stringBuilder.append("\n");
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示")
                    .setMessage(stringBuilder.toString())
                    .setPositiveButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (presenter != null) {
                                //presenter.upData(createBill(),list);
                            }
                        }
                    })
                    .setNegativeButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (WlCodeCheckNet net : wlCodeCheckNets) {
                                for (Code code : list) {
                                    if (!TextUtils.isEmpty(code.getCode())) {
                                        if (code.getCode().equals(net.getCode())) {
                                            Logger.i(code.getCode() + "--" + net.getCode());
                                            list.remove(code);
                                        }
                                    }
                                }
                            }

                            adapter.notifyDataSetChanged();
                            count();

                            if (presenter != null) {
                                presenter.upData(createBill(true), list);
                            }
                        }
                    });
            builder.create().show();
        }
    }

    @Override
    public void err(String err) {
        Toasty.error(this, err, Toast.LENGTH_LONG).show();
    }

    @Override
    public void success(String success) {
        Toasty.success(this, success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void upSuccess() {
        list.clear();
        adapter.notifyDataSetChanged();
        count();
    }

    @Override
    public void count() {
        if (list != null) {
            tvCount.setText(String.valueOf(list.size()));

            if(list.size() == 0){
                etDealerid.setEnabled(true);
            }else{
                etDealerid.setEnabled(false);
            }
        }
    }

    @Override
    public void installIscan() {
        if (SharedPreUtil.getSetIntallIsan(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("安装ISCAN?");
            builder.setMessage("没有发现扫描程序ISCAN，是否安装ISCAN？" +
                    "如果此设备是采集器，请安装ISCAN，如果是手机请选择不再提醒。");
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (presenter != null) {
                        presenter.installIscan();
                    }
                }
            });
            builder.setNeutralButton("不再提醒", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreUtil.saveSetIntallIsan(OutActivity.this, false);
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void delete(int position) {
        list.remove(position);
        adapter.notifyDataSetChanged();
        count();
    }

    @OnClick(R.id.ll_ac_out_keyword)
    public void keyword() {
        final InputDialog inputDialog = new InputDialog(this);
        inputDialog.setTitle("设置经销商筛选字");
        inputDialog.setMaterialEditText("请输入关键字！");
        inputDialog.setInputType(InputType.TYPE_CLASS_TEXT);
        if (!TextUtils.isEmpty(dealersKeyword)) {
            inputDialog.setContent(dealersKeyword);
        }
        inputDialog.setOnclickListeners(new InputDialog.OnClickListeners() {
            @Override
            public void sure() {
                SharedPreUtil.saveOutDealerKeyword(OutActivity.this, inputDialog.getContent().trim());
                dealersKeyword = inputDialog.getContent();
                tvKeyword.setText(dealersKeyword);
                if (presenter != null) {
                    presenter.getDealers();
                }
            }

            @Override
            public void cancel() {

            }
        });
        inputDialog.show();
    }

    @OnTextChanged(R.id.et_ac_out_dealerid)
    public void onDealerIdChange(CharSequence s, int start, int before, int count){
        Logger.i(s.toString());

        String dealerName = DealerHelper.getSingleton(OutActivity.this).getDealerName(s.toString().trim());
        if(!TextUtils.isEmpty(dealerName)){
            dealerId = s.toString().trim();

            tvDealer.setText(dealerName);
        }else{
            dealerId = "";

            tvDealer.setText("经销商");
        }
    }


    private class CodeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 此处获取扫描结果信息
            String scanResult = intent.getStringExtra("value");

            if (TextUtils.isEmpty(scanResult)) {
                Toasty.error(OutActivity.this, "物流码为空", Toast.LENGTH_LONG).show();
            } else {
                addCode(TextUtil.filterCode(scanResult));
            }
        }

    }

    @OnClick(R.id.iv_ac_out_scan)
    public void scan() {
        if (Camera.getNumberOfCameras() <= 0) {
            Toasty.error(this, "该设备没有相机！！！", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(dealerId)) {
            Toasty.error(this, "请先选择经销商！！！", Toast.LENGTH_LONG).show();
            return;
        }

        //大于6.0申请动态权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtils.checkSelfPermission(this, Manifest.permission.CAMERA)) {
                Intent intent = new Intent();
                intent.setClass(this, ScanCodeActivity.class);
                startActivityForResult(intent, SCAN_CODE_REQUEST);
            } else {
                PermissionUtils.requestPermission(this, PERMISION_CAMERA, Manifest.permission.CAMERA);
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(this, ScanCodeActivity.class);
            startActivityForResult(intent, SCAN_CODE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_CODE_REQUEST) {
            if (resultCode == ScanCodeActivity.SCAN_CODE_RESULT_SINGLE) {
                String scanResult = data.getStringExtra(ScanCodeActivity.CODE);
                addCode(TextUtil.filterCode(scanResult));
            } else if (resultCode == ScanCodeActivity.SCAN_CODE_RESULT_MULTIPLE) {
                List<String> scanResults = data.getStringArrayListExtra(ScanCodeActivity.CODE);
                for (String scanResult : scanResults) {
                    addCode(TextUtil.filterCode(scanResult));
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISION_CAMERA) {
            if (PermissionUtils.getPermissionResult(grantResults)) {
                Intent intent = new Intent();
                intent.setClass(this, ScanCodeActivity.class);
                startActivityForResult(intent, SCAN_CODE_REQUEST);
            }
        }

    }

    @OnClick(R.id.ll_ac_out_back)
    public void back() {
        if (list != null && list.size() > 0) {
            exitDealBill();
        } else {
            finish();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//点击的是返回键
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {//按键的按下事件

                if (list != null && list.size() > 0) {
                    exitDealBill();
                    return true;
                }

            } else if (event.getAction() == KeyEvent.ACTION_UP && event.getRepeatCount() == 0) {//按键的抬起事件
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void exitDealBill() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("发现未保存的物流码,是否保存?");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.saveBillInDB(createBill(false), list);
                list.clear();
                adapter.notifyDataSetChanged();
                count();

                finish();
            }
        });
        builder.setNeutralButton("上传", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                up();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (immersionBar != null) {
            immersionBar.destroy();
        }

        scannerInerface.close();
        unregisterReceiver(codeReceiver);
    }
}
