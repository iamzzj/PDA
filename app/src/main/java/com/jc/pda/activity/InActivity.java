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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.barcodescandemo.ScannerInerface;
import com.bigkoo.pickerview.OptionsPickerView;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.jc.pda.R;
import com.jc.pda.adapter.CodeAdapter;
import com.jc.pda.component.DaggerInActivityComponent;
import com.jc.pda.database.helper.CodeHelper;
import com.jc.pda.entity.Batch;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;
import com.jc.pda.entity.Product;
import com.jc.pda.entity.ProductAndBatch;
import com.jc.pda.entity.UpDate;
import com.jc.pda.entitynet.WlCodeCheckNet;
import com.jc.pda.module.ContextModule;
import com.jc.pda.module.InActivityModule;
import com.jc.pda.presenter.InActivityPresenter;
import com.jc.pda.presenter.view.InActivityView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.PermissionUtils;
import com.jc.pda.utils.SharedPreUtil;
import com.jc.pda.utils.TextUtil;
import com.jc.pda.utils.TimeUtils;
import com.jc.pda.view.HorizontalDividerItemDecoration;
import com.jc.pda.view.InputDialog;
import com.orhanobut.logger.Logger;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class InActivity extends AppCompatActivity implements InActivityView, CodeAdapter.RightOnItemClickListener {
    public static final int SCAN_CODE_REQUEST = 10000;
    private static final int PERMISION_CAMERA = 10001;
    @BindView(R.id.rv_ac_in_codes)
    RecyclerView rvAcInCodes;
    @BindView(R.id.tv_ac_in_product)
    TextView tvAcInProduct;
    @BindView(R.id.tv_ac_in_batch)
    TextView tvAcInBatch;
    @BindView(R.id.v_ac_in_bar)
    View vAcInBar;
    @BindView(R.id.iv_ac_in_input)
    ImageView ivAcInInput;
    @BindView(R.id.sb_ac_in_code_type)
    SwitchButton sbSBType;
    @BindView(R.id.tv_ac_in_count)
    TextView tvCount;
    @BindView(R.id.tv_ac_in_up)
    TextView tvUp;
    private ImmersionBar immersionBar;

    @Inject
    InActivityPresenter presenter;

    SweetAlertDialog dialog;

    //选项
    private OptionsPickerView optionProduct;
    private List<String> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();

    private List<Product> products = new ArrayList<>();
    private List<List<Batch>> batchs = new ArrayList<>();

    private String productId, batchId;

    //条码
    private List<Code> list;
    private CodeAdapter adapter;
    private ScannerInerface scannerInerface = new ScannerInerface(this);
    private CodeBroadcastReceiver codeReceiver = new CodeBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in);
        ButterKnife.bind(this);

        DaggerInActivityComponent.builder()
                .contextModule(new ContextModule(this))
                .inActivityModule(new InActivityModule(this))
                .build()
                .inject(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            vAcInBar.setVisibility(View.VISIBLE);
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

        optionProduct = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                if (options1 < options1Items.size() &&options1 >=0){
                    tvAcInProduct.setText(options1Items.get(options1));
                }

                if (options1 < options2Items.size() && options2 < options2Items.get(options1).size()&&options1 >=0&&options2>=0){
                    tvAcInBatch.setText(options2Items.get(options1).get(options2));
                }

                if (options1 < products.size()&&options1 >=0){
                    productId = products.get(options1).getProductNo();
                }
                if (options1 < batchs.size() && options2 < batchs.get(options1).size() &&options1 >=0&& options2>=0){
                    batchId = batchs.get(options1).get(options2).getBatchNo();
                }

                Logger.i(productId + "--" + batchId);
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

        rvAcInCodes.setLayoutManager(new LinearLayoutManager(this));
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(ContextCompat.getColor(this, R.color.divider));
        rvAcInCodes.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).build());
        //rvProductProducts.setNestedScrollingEnabled(false);

        list = new ArrayList<>();
        adapter = new CodeAdapter(this, list);
        adapter.setRightOnItemClickListener(this);
        rvAcInCodes.setAdapter(adapter);

        // IDATA 广播接收条码
        scannerInerface.open();
        scannerInerface.enablePlayBeep(true);
        scannerInerface.setOutputMode(1);//使用广播模式0为模拟输出
        scannerInerface.lockScanKey();
        scannerInerface.enablePlayVibrate(true);

        IntentFilter mFilter = new IntentFilter("android.intent.action.SCANRESULT");
        registerReceiver(codeReceiver, mFilter);

        if(presenter!=null){
            presenter.getProductAndBatch();
            presenter.havePackge();
        }
    }

    @OnClick(R.id.ll_ac_in_choose)
    public void choose() {
        if(list!=null&&list.size()>0){
            err("请先保存或上传订单");
        }else{
            if (optionProduct != null) {
                optionProduct.show();
            }
        }
    }

    /**
     * false 条码  true 产品码
     *
     * @return
     */
    @Override
    public boolean getSBType() {
        return sbSBType.isChecked();
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

        tvUp.setTextColor(ContextCompat.getColor(this,R.color.title_back));
        tvUp.setEnabled(true);
    }

    @Override
    public String getCodeValue() {
        return SharedPreUtil.getUserCID(this);
    }

    @Override
    public String getCodeType() {
        return "01";
    }

    @Override
    public void setWlCodeCheckSuccess() {
        if (presenter != null) {
            presenter.upData(createBill(true),list);
        }
    }

    @Override
    public void setWlCodeCheckFail(final List<WlCodeCheckNet> wlCodeCheckNets) {
        if (wlCodeCheckNets != null && wlCodeCheckNets.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("发现与服务器订单重复码\n是否移除下列重复码？\n\n");
            for (int i = 0; i < wlCodeCheckNets.size(); i++) {
                stringBuilder.append(wlCodeCheckNets.get(i).getCode());
                if (i < wlCodeCheckNets.size() - 1) {
                    stringBuilder.append("\n");
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("温馨提示")
                    .setMessage(stringBuilder.toString())
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (presenter != null) {
                                //presenter.upData(createBill(),list);
                            }
                        }
                    })
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
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
                                presenter.upData(createBill(true),list);
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
        }
    }

    @Override
    public void installIscan() {
        if(SharedPreUtil.getSetIntallIsan(this)) {
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
                    SharedPreUtil.saveSetIntallIsan(InActivity.this, false);
                }
            });
            builder.create().show();
        }
    }

    @Override
    public void setProdutAndBatch(ProductAndBatch produtAndBatch) {
        if (produtAndBatch != null) {
            options1Items = produtAndBatch.getProductStrings();
            options2Items = produtAndBatch.getBatchStrings();
            if (optionProduct != null) {
                optionProduct.setPicker(produtAndBatch.getProductStrings(), produtAndBatch.getBatchStrings());
            }

            products = produtAndBatch.getProducts();
            batchs = produtAndBatch.getBatchs();
        }
    }

    @Override
    public void setgetGoodInfoByUPCSuccess() {
        if(sbSBType !=null){
            Toasty.success(this,"验证商品码成功，扫码状态变为扫物流码！",Toast.LENGTH_SHORT).show();
            sbSBType.setChecked(false);
        }
    }

    @OnClick(R.id.iv_ac_in_input)
    public void input() {
        final InputDialog inputDialog = new InputDialog(this);
        if (getSBType()) {
            inputDialog.setTitle("商品码");
            inputDialog.setMaterialEditText("请输入商品码！");
            inputDialog.setOnclickListeners(new InputDialog.OnClickListeners() {
                @Override
                public void sure() {
                    if (presenter != null) {
                        presenter.getGoodInfoByUPC(inputDialog.getContent());
                    }
                }

                @Override
                public void cancel() {

                }
            });
        } else {
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
        }
        inputDialog.show();
    }


    @OnClick(R.id.tv_ac_in_up)
    public void up() {
        if (list == null || list.size() == 0) {
            Toasty.error(this, "列表中没有物流码，请扫码后再试！", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(productId) || TextUtils.isEmpty(batchId)) {
            Toasty.error(this, "请先选择产品，批号！！！", Toast.LENGTH_LONG).show();
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

        tvUp.setTextColor(ContextCompat.getColor(this,R.color.divider));
        tvUp.setEnabled(false);
        presenter.wlCodeCheck(Codes);
    }

    private Bill createBill(boolean isY) {
        //本地生成一个订单
        UpDate upDate = new UpDate();
        Bill bill = new Bill();
        bill.setBillId(TimeUtils.createBillNo(TimeUtils.BillTye.RUKU, true ,isY));
        bill.setProductId(productId);
        bill.setBatchId(batchId);
        bill.setWholesaleId("");
        bill.setDealerId("");
        bill.setShopId("");
        bill.setBillStyle(Constant.IN);
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
        if (TextUtils.isEmpty(productId) || TextUtils.isEmpty(batchId)) {
            Toasty.error(this, "请先选择产品，批号！！！", Toast.LENGTH_LONG).show();
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
        if (CodeHelper.getSingleton(this).haveCode(codeStr, Constant.IN)) {
            Toasty.error(this, "以前的订单中已有该物流码", Toast.LENGTH_LONG).show();
            return;
        }
        UpDate upDate = new UpDate();
        Code code = new Code();
        code.setCode(codeStr);
        code.setProductId(productId);
        code.setBatchId(batchId);
        code.setWholesaleId("");
        code.setDealerId("");
        code.setShopId("");
        code.setCodeStyle(Constant.IN);
        code.setUpType(Constant.FISTUP);
        code.setAdminId(SharedPreUtil.getLoginUser(this));
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
    public void delete(int position) {
        list.remove(position);
        adapter.notifyDataSetChanged();
        count();
    }

    private class CodeBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 此处获取扫描结果信息
            String scanResult = intent.getStringExtra("value");

            if (TextUtils.isEmpty(scanResult)) {
                Toasty.error(InActivity.this, "物流码为空", Toast.LENGTH_LONG).show();
            } else {
                if (getSBType()) {
                    if (presenter != null) {
                        presenter.getGoodInfoByUPC(TextUtil.filterCode(scanResult));
                    }
                } else {
                    addCode(TextUtil.filterCode(scanResult));
                }
            }
        }
    }

    @OnClick(R.id.iv_ac_in_scan)
    public void scan() {
        if (Camera.getNumberOfCameras() <= 0) {
            Toasty.error(this, "该设备没有相机！！！", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(productId) || TextUtils.isEmpty(batchId)) {
            Toasty.error(this, "请先选择产品，批号！！！", Toast.LENGTH_LONG).show();
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
                if (getSBType()) {
                    if (presenter != null) {
                        presenter.getGoodInfoByUPC(TextUtil.filterCode(scanResult));
                    }
                } else {
                    addCode(TextUtil.filterCode(scanResult));
                }
            } else if (resultCode == ScanCodeActivity.SCAN_CODE_RESULT_MULTIPLE) {
                if (getSBType()) {
                    success("商品码只用单个扫描,请用单个扫码");
                    return;
                }

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

    @OnClick(R.id.ll_ac_in_back)
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
