package com.jc.pda.presenter.view;

import com.jc.pda.entity.Bill;
import com.jc.pda.entity.Code;

import java.util.List;

/**
 * Created by z on 2018/1/5.
 */

public interface BillActivityView {
    String getBillId();
    void setCodes(List<Code> codes);
    void setBill(Bill bill);
}
