package com.jc.pda.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.jc.pda.database.helper.BillHelper;
import com.jc.pda.database.helper.CodeHelper;
import com.jc.pda.database.helper.DealerHelper;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;
import com.jc.pda.entity.Dealer;
import com.jc.pda.entity.UpData;
import com.jc.pda.presenter.view.BackActivityView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.Global;
import com.jc.pda.utils.SharedPreUtil;
import com.jc.pda.web.UpFile;
import com.jc.pda.web.WebService;

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
 * Created by z on 2018/1/4.
 */

public class BackActivityPresenter extends InOutBackPresenter{
    private Context context;
    private BackActivityView backActivityView;

    @Inject
    public BackActivityPresenter(Context context, BackActivityView backActivityView) {
        super(context,backActivityView);

        this.context = context;
        this.backActivityView = backActivityView;
    }

    public void getDealers(){
        Observable.create(new ObservableOnSubscribe<List<Dealer>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Dealer>> e) throws Exception {
                List<Dealer> dealers = DealerHelper.getSingleton(context).getDealers();
                e.onNext(dealers);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Dealer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Dealer> dealers) {
                        List<String> ops = new ArrayList<String>();
                        if(dealers != null && dealers.size()>0){
                            for (Dealer dealer : dealers){
                                ops.add(dealer.getDealerName());
                            }
                        }
                        backActivityView.setDealers(ops,dealers);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void upData(final Bill bill, final List<Code> codes){
        if(codes.size()==0){
            backActivityView.err("列表没有条码");
            return;
        }
        backActivityView.dialogShow("正在上传文件");
        Observable.create(new ObservableOnSubscribe<UpData>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<UpData> e) throws Exception {
                String actionUrl = Global.URLOUTBACK;
                String filename =UpFile.createUpFileName(context,bill.getBillId(), Constant.BACK);
                String content = UpFile.createUpFileContent(bill,codes,context,Constant.BACK);

                boolean success = UpFile.upFile(actionUrl,null,filename,content);
                e.onNext(new UpData(success,bill,codes));
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
                        bill.setUp(true);
                        saveBillInDB(bill,codes);

                        backActivityView.success("上传成功");
                        backActivityView.upSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        backActivityView.err("上传出错\n"+e.getMessage());
                        backActivityView.dialogDismiss();
                    }

                    @Override
                    public void onComplete() {
                        backActivityView.dialogDismiss();
                    }
                });
    }

    public void saveBillInDB(Bill bill, List<Code> codes){
        BillHelper.getSingleton(context).insertBill(bill);

        for(Code code : codes){
            code.setBillId(bill.getBillId());
        }

        CodeHelper.getSingleton(context).insertCodes(codes);
    }

    /**
     * 验证退货码
     */
    public void checkProductInfo(final String code){
        if(TextUtils.isEmpty(backActivityView.getDealerId())){
            backActivityView.err("请选择经销商!!!");
            return;
        }
        backActivityView.dialogShow("正在验证退货码");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Object[] strs = {
                        "GuestID",backActivityView.getDealerId(),
                        "Code",code
                };
                String result = WebService.sendWebService("CheckProductInfo",strs);

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
                        backActivityView.err("验证退货码异常\n"+e.getMessage());
                        backActivityView.dialogDismiss();
                    }

                    @Override
                    public void onComplete() {
                        backActivityView.dialogDismiss();
                    }
                });
    }
}
