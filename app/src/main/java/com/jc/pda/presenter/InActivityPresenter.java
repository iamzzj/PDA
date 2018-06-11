package com.jc.pda.presenter;

import android.content.Context;

import com.jc.pda.database.helper.BatchHelper;
import com.jc.pda.database.helper.BillHelper;
import com.jc.pda.database.helper.CodeHelper;
import com.jc.pda.database.helper.ProductHelper;
import com.jc.pda.entity.Batch;
import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;
import com.jc.pda.entity.Product;
import com.jc.pda.entity.ProductAndBatch;
import com.jc.pda.entity.UpData;
import com.jc.pda.entitynet.GoodInfoByUPC;
import com.jc.pda.entitynet.GoodInfoByUPCGood;
import com.jc.pda.entitynet.WlCodeCheckNet;
import com.jc.pda.presenter.view.InActivityView;
import com.jc.pda.utils.Constant;
import com.jc.pda.utils.Global;
import com.jc.pda.utils.JsonUtils;
import com.jc.pda.utils.SharedPreUtil;
import com.jc.pda.web.UpFile;
import com.jc.pda.web.WebService;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
 * Created by z on 2017/12/28.
 */

public class InActivityPresenter extends InOutBackPresenter{
    private Context context;
    private InActivityView inActivityView;

    @Inject
    public InActivityPresenter(Context context, InActivityView inActivityView) {
        super(context,inActivityView);

        this.context = context;
        this.inActivityView = inActivityView;
    }
    
    public void getProductAndBatch(){
        Observable.create(new ObservableOnSubscribe<ProductAndBatch>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ProductAndBatch> e) throws Exception {
                List<Product> products = ProductHelper.getSingleton(context).getProducts();
                List<List<Batch>> batchs = new ArrayList<List<Batch>>();

                List<String> productStrings = new ArrayList<String>();
                List<List<String>> batchStrings = new ArrayList<List<String>>();

                if(products!=null&&products.size()>0) {
                    for (Product product : products) {
                        List<Batch> bs = BatchHelper.getSingleton(context).getBatchs(product.getProductNo());
                        batchs.add(bs);

                        productStrings.add(product.getProductName());

                        List<String> b = new ArrayList<String>();
                        if(bs!=null && bs.size()>0){
                            for (Batch batch : bs){
                                b.add(batch.getBatchName());
                            }
                        }
                        batchStrings.add(b);
                    }
                }

                e.onNext(new ProductAndBatch(productStrings,batchStrings,products,batchs));
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProductAndBatch>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProductAndBatch productAndBatch) {
                        inActivityView.setProdutAndBatch(productAndBatch);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 扫码验证产品
     */
    public void getGoodInfoByUPC(final String code){
        inActivityView.dialogShow("产品码查询");
        Observable.create(new ObservableOnSubscribe<ProductAndBatch>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ProductAndBatch> e) throws Exception {
                Object[] strs = {
                        "CodeValue",inActivityView.getCodeValue(),
                        "cUPC",code
                };
                String result = WebService.sendWebService("GetGoodInfoByUPC",strs);
                GoodInfoByUPC goodInfoByUPC = JsonUtils.string2Object(result,GoodInfoByUPC.class);

                if(goodInfoByUPC.getSuccess()){
                    GoodInfoByUPCGood good = JsonUtils.string2Object(goodInfoByUPC.getData(),GoodInfoByUPCGood.class);
                    Product p = new Product();
                    p.setProductNo(good.getcGoodsCode());
                    p.setProductName(good.getcGoodsName());
                    List<Product> products = new ArrayList<Product>();
                    products.add(p);

                    List<List<Batch>> batchs = new ArrayList<List<Batch>>();
                    List<String> productStrings = new ArrayList<String>();
                    List<List<String>> batchStrings = new ArrayList<List<String>>();
                    if(products!=null&&products.size()>0) {
                        for (Product product : products) {
                            List<Batch> bs = BatchHelper.getSingleton(context).getBatchs(product.getProductNo());
                            batchs.add(bs);
                            productStrings.add(product.getProductName());
                            List<String> b = new ArrayList<String>();
                            if(bs!=null && bs.size()>0){
                                for (Batch batch : bs){
                                    b.add(batch.getBatchName());
                                }
                            }
                            batchStrings.add(b);
                        }
                    }
                    e.onNext(new ProductAndBatch(productStrings,batchStrings,products,batchs));
                }else{
                    if(goodInfoByUPC.getErrors()!=null&&goodInfoByUPC.getErrors().size()>0){
                        e.onError(new Throwable(goodInfoByUPC.getErrors().get(0)));
                    }
                }

                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProductAndBatch>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ProductAndBatch productAndBatch) {
                        inActivityView.setProdutAndBatch(productAndBatch);
                        inActivityView.setgetGoodInfoByUPCSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        inActivityView.err("查询产品码出错\n"+e.getMessage());
                        inActivityView.dialogDismiss();
                    }

                    @Override
                    public void onComplete() {
                        inActivityView.dialogDismiss();
                    }
                });
    }

    public void upData(final Bill bill, final List<Code> codes){
        if(codes.size()==0){
            inActivityView.err("列表没有条码");
            return;
        }
        inActivityView.dialogShow("正在上传文件");
        Observable.create(new ObservableOnSubscribe<UpData>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<UpData> e) throws Exception {
                String actionUrl = Global.URLIN;
                //String filename = bill.getProductId()+"_"+bill.getBillId()+".l.txt";
                String filename = UpFile.createUpFileName(context,bill.getBillId(), Constant.IN);

                String content = UpFile.createUpFileContent(bill,codes,context,Constant.IN);

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

                        inActivityView.success("上传成功");
                        inActivityView.upSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        inActivityView.err("上传出错\n"+e.getMessage());
                        inActivityView.dialogDismiss();
                    }

                    @Override
                    public void onComplete() {
                        inActivityView.dialogDismiss();
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
