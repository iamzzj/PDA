package com.jc.pda.presenter;

import android.content.Context;

import com.jc.pda.database.helper.BillHelper;
import com.jc.pda.database.helper.CodeHelper;
import com.jc.pda.database.helper.DealerHelper;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;
import com.jc.pda.entity.Dealer;
import com.jc.pda.entity.UpData;
import com.jc.pda.presenter.view.OutActivityView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.Global;
import com.jc.pda.utils.SharedPreUtil;
import com.jc.pda.web.UpFile;

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
 * Created by z on 2018/1/3.
 */

public class OutActivityPresenter extends InOutBackPresenter{
    private Context context;
    private OutActivityView outActivityView;

    @Inject
    public OutActivityPresenter(Context context, OutActivityView outActivityView) {
        super(context,outActivityView);

        this.context = context;
        this.outActivityView = outActivityView;
    }

    public void getDealers(){
        Observable.create(new ObservableOnSubscribe<List<Dealer>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Dealer>> e) throws Exception {
                List<Dealer> dealers = DealerHelper.getSingleton(context).getDealersByKey(outActivityView.dealersKeyword());
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
                        outActivityView.setDealers(ops,dealers);
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
            outActivityView.err("列表没有条码");
            return;
        }
        outActivityView.dialogShow("正在上传文件");
        Observable.create(new ObservableOnSubscribe<UpData>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<UpData> e) throws Exception {
                String actionUrl = Global.URLOUTBACK;
                String filename =UpFile.createUpFileName(context,bill.getBillId(), Constant.OUT);
                String content = UpFile.createUpFileContent(bill,codes,context,Constant.OUT);

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

                        outActivityView.success("上传成功");
                        outActivityView.upSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        outActivityView.err("上传出错\n"+e.getMessage());
                        outActivityView.dialogDismiss();
                    }

                    @Override
                    public void onComplete() {
                        outActivityView.dialogDismiss();
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
}
