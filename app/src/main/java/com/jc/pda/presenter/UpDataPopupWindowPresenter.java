package com.jc.pda.presenter;

import android.content.Context;

import com.jc.pda.database.helper.BillHelper;
import com.jc.pda.database.helper.CodeHelper;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;
import com.jc.pda.entity.UpData;
import com.jc.pda.entity.UpDate;
import com.jc.pda.presenter.view.UpDataPopupWindowView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.Global;
import com.jc.pda.utils.SaveText;
import com.jc.pda.utils.SharedPreUtil;
import com.jc.pda.utils.TimeUtils;
import com.jc.pda.web.UpFile;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by z on 2018/1/9.
 */

public class UpDataPopupWindowPresenter {
    private Context context;
    private UpDataPopupWindowView upDataPopupWindowView;

    @Inject
    public UpDataPopupWindowPresenter(Context context, UpDataPopupWindowView upDataPopupWindowView) {
        this.context = context;
        this.upDataPopupWindowView = upDataPopupWindowView;
    }

    public void getBill() {
        Observable.create(new ObservableOnSubscribe<Bill>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Bill> e) throws Exception {
                Bill bill = BillHelper.getSingleton(context).getBillByBillId(upDataPopupWindowView.getBillId());
                e.onNext(bill);
                Logger.i(bill.toString());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bill>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Bill bill) {
                        upDataPopupWindowView.setBill(bill);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getCodes() {
        Observable.create(new ObservableOnSubscribe<List<Code>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Code>> e) throws Exception {
                List<Code> codes = CodeHelper.getSingleton(context).getCodesByBillId(upDataPopupWindowView.getBillId());
                e.onNext(codes);
                Logger.i(codes.toString());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Code>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Code> codes) {
                        upDataPopupWindowView.setCodes(codes);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void saveTxt(final Bill bill, final List<Code> codes) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                if (bill.getBillStyle() == Constant.IN) {
                    String filename = bill.getBillId() + ".txt";
                    String content = UpFile.createUpFileContent(bill,codes,context,Constant.IN);

                    SaveText.saveText(filename, content);
                } else if (bill.getBillStyle() == Constant.OUT) {
                    String filename = bill.getBillId() + ".txt";
                    String content = UpFile.createUpFileContent(bill,codes,context,Constant.OUT);
                    SaveText.saveText(filename, content);
                }else if(bill.getBillStyle() == Constant.BACK){
                    String filename = bill.getBillId()+".txt";
                    String content = UpFile.createUpFileContent(bill,codes,context,Constant.BACK);
                    SaveText.saveText(filename, content);
                }
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
                    public void onNext(@NonNull String s) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        upDataPopupWindowView.err("保存异常\n"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        upDataPopupWindowView.success("导出成功\n"+"请在内存卡"+Global.NAME+"OUT文件夹查看");
                    }
                });

    }

    public void upBill(final Bill bill, final List<Code> codes){
        upDataPopupWindowView.dialogShow("正在上传");
        Observable.create(new ObservableOnSubscribe<UpData>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<UpData> e) throws Exception {
                if (bill.getBillStyle() == Constant.IN) {
                    String actionUrl = Global.URLIN;
                    String filename = UpFile.createUpFileName(context,bill.getBillId(),Constant.IN);
                    String content = UpFile.createUpFileContent(bill,codes,context,Constant.IN);

                    boolean success = UpFile.upFile(actionUrl,null,filename,content);
                    e.onNext(new UpData(success,bill,codes));
                }else if(bill.getBillStyle() == Constant.OUT){
                    String actionUrl = Global.URLOUTBACK;
                    String filename = UpFile.createUpFileName(context,bill.getBillId(),Constant.OUT);
                    String content = UpFile.createUpFileContent(bill,codes,context,Constant.OUT);

                    boolean success = UpFile.upFile(actionUrl,null,filename,content);
                    e.onNext(new UpData(success,bill,codes));
                }else if(bill.getBillStyle() == Constant.BACK){
                    String actionUrl = Global.URLOUTBACK;
                    String filename = UpFile.createUpFileName(context,bill.getBillId(),Constant.BACK);
                    String content = UpFile.createUpFileContent(bill,codes,context,Constant.BACK);

                    boolean success = UpFile.upFile(actionUrl,null,filename,content);
                    e.onNext(new UpData(success,bill,codes));
                }
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull UpData upData) {
                        if(upData.isSuccess()){
                            Bill bill = upData.getBill();
                            bill.setUp(true);
                            BillHelper.getSingleton(context).upDataBillUP(bill);
                            upDataPopupWindowView.setBillChange(bill);

                            upDataPopupWindowView.success("上传成功");
                        }else{
                            upDataPopupWindowView.err("上传失败");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        upDataPopupWindowView.dialogDismiss();
                        upDataPopupWindowView.err("上传异常\n"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        upDataPopupWindowView.dialogDismiss();
                    }
                });
    }

    public void reupBill(final Bill oldbill, final List<Code> codes){
        upDataPopupWindowView.dialogShow("正在重复上传");
        Observable.create(new ObservableOnSubscribe<UpData>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<UpData> e) throws Exception {
                if (oldbill.getBillStyle() == Constant.IN) {

                    UpDate upDate = new UpDate();
                    Bill bill = new Bill();
                    bill.setBillId(TimeUtils.createBillNo(TimeUtils.BillTye.RUKU, false ,false));
                    bill.setProductId(oldbill.getProductId());
                    bill.setBatchId(oldbill.getBatchId());
                    bill.setWholesaleId("");
                    bill.setDealerId("");
                    bill.setShopId("");
                    bill.setBillStyle(Constant.IN);
                    bill.setUpType(Constant.REUP);
                    bill.setUp(true);
                    bill.setOldBillNo(oldbill.getBillId());
                    bill.setAdminId(SharedPreUtil.getLoginUser(context));
                    bill.setUpDateString(upDate.getUpDate());
                    bill.setUpDateInt(upDate.getTime());
                    bill.setDelete(false);

                    String actionUrl = Global.URLIN;
                    String filename = UpFile.createUpFileName(context,bill.getBillId(),Constant.IN);
                    String content = UpFile.createUpFileContent(bill,codes,context,Constant.IN);

                    boolean success = UpFile.upFile(actionUrl,null,filename,content);
                    e.onNext(new UpData(success,bill,codes));
                }else if(oldbill.getBillStyle() == Constant.OUT){

                    UpDate upDate = new UpDate();
                    Bill bill = new Bill();
                    bill.setBillId(TimeUtils.createBillNo(TimeUtils.BillTye.CHUKU, false,false));
                    bill.setProductId("");
                    bill.setBatchId("");
                    bill.setWholesaleId("");
                    bill.setDealerId(oldbill.getDealerId());
                    bill.setShopId("");
                    bill.setBillStyle(Constant.OUT);
                    bill.setUpType(Constant.REUP);
                    bill.setUp(true);
                    bill.setOldBillNo(oldbill.getBillId());
                    bill.setAdminId(SharedPreUtil.getLoginUser(context));
                    bill.setUpDateString(upDate.getUpDate());
                    bill.setUpDateInt(upDate.getTime());
                    bill.setDelete(false);

                    String actionUrl = Global.URLOUTBACK;
                    String filename = UpFile.createUpFileName(context,bill.getBillId(),Constant.OUT);
                    String content = UpFile.createUpFileContent(bill,codes,context,Constant.OUT);

                    boolean success = UpFile.upFile(actionUrl,null,filename,content);
                    e.onNext(new UpData(success,bill,codes));
                }else if(oldbill.getBillStyle() == Constant.BACK){
                    UpDate upDate = new UpDate();
                    Bill bill = new Bill();
                    bill.setBillId(TimeUtils.createBillNo(TimeUtils.BillTye.TUIHUO, false,false));
                    bill.setProductId("");
                    bill.setBatchId("");
                    bill.setWholesaleId("");
                    bill.setDealerId(oldbill.getDealerId());
                    bill.setShopId("");
                    bill.setBillStyle(Constant.BACK);
                    bill.setUpType(Constant.REUP);
                    bill.setUp(true);
                    bill.setOldBillNo(oldbill.getBillId());
                    bill.setAdminId(SharedPreUtil.getLoginUser(context));
                    bill.setUpDateString(upDate.getUpDate());
                    bill.setUpDateInt(upDate.getTime());
                    bill.setDelete(false);

                    String actionUrl = Global.URLOUTBACK;
                    String filename = UpFile.createUpFileName(context,bill.getBillId(),Constant.BACK);
                    String content = UpFile.createUpFileContent(bill,codes,context,Constant.BACK);

                    boolean success = UpFile.upFile(actionUrl,null,filename,content);
                    e.onNext(new UpData(success,bill,codes));
                }
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpData>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull UpData upData) {
                        if(upData.isSuccess()){

                            BillHelper.getSingleton(context).insertBill(upData.getBill());
                            List<Code> codes = new ArrayList<Code>();
                            for(Code c: upData.getCodes()){
                                c.setBillId(upData.getBill().getBillId());
                                codes.add(c);
                            }
                            CodeHelper.getSingleton(context).insertCodes(codes);

                            upDataPopupWindowView.success("重复上传成功");
                        }else{
                            upDataPopupWindowView.err("重复上传失败");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        upDataPopupWindowView.dialogDismiss();
                        upDataPopupWindowView.err("重复上传异常\n"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        upDataPopupWindowView.dialogDismiss();
                    }
                });
    }
}
