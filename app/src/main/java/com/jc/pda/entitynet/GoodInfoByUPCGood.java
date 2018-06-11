package com.jc.pda.entitynet;

/**
 * Created by z on 2018/1/10.
 */

public class GoodInfoByUPCGood {
    private String cGoodsCode;
    private String cGoodsName;

    public String getcGoodsCode() {
        return cGoodsCode;
    }

    public void setcGoodsCode(String cGoodsCode) {
        this.cGoodsCode = cGoodsCode;
    }

    public String getcGoodsName() {
        return cGoodsName;
    }

    public void setcGoodsName(String cGoodsName) {
        this.cGoodsName = cGoodsName;
    }

    @Override
    public String toString() {
        return "GoodInfoByUPCGood{" +
                "cGoodsCode='" + cGoodsCode + '\'' +
                ", cGoodsName='" + cGoodsName + '\'' +
                '}';
    }
}
