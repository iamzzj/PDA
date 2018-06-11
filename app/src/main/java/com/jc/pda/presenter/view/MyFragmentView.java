package com.jc.pda.presenter.view;

/**
 * Created by z on 2017/12/22.
 */

public interface MyFragmentView {
    String getCodeValue();
    void success(String success);
    void err(String err);
    void info(String info);
    void dialogShow();
    void dialogDismiss();
    void setCount(int productCount,int batchCount,int dealerCount);
}
