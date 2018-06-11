package com.jc.pda.presenter.view;

import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;

import java.util.List;

/**
 * Created by z on 2018/1/9.
 */

public interface UpDataPopupWindowView {
    void setBill(Bill bill);
    void setBillChange(Bill bill);
    void setCodes(List<Code> codes);
    String getBillId();
    void dialogShow(String title);
    void dialogDismiss();
    void success(String success);
    void err(String err);
}
