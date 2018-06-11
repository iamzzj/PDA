package com.jc.pda.entitynet;

/**
 * Created by z on 2018/1/8.
 */

public class WlCodeCheckNet {
    private String CodeType;
    private String cBillID;
    private String Code;

    public void setCodeType(String CodeType) {
        this.CodeType = CodeType;
    }

    public String getCodeType() {
        return CodeType;
    }

    public void setCBillID(String cBillID) {
        this.cBillID = cBillID;
    }

    public String getCBillID() {
        return cBillID;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getCode() {
        return Code;
    }

    @Override
    public String toString() {
        return "WlCodeCheckNet{" +
                "CodeType='" + CodeType + '\'' +
                ", cBillID='" + cBillID + '\'' +
                ", Code='" + Code + '\'' +
                '}';
    }
}
