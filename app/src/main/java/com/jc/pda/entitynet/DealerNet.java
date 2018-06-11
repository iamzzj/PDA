package com.jc.pda.entitynet;

/**
 * Created by z on 2017/12/25.
 */

public class DealerNet {

    private String cID;
    private String cUserAccountID;
    private String cSaleClassName;
    private String cGuestCode;
    private String cGuestName;
    private String cGuestElseCode;
    private String cChannelName;
    private String cChainName;
    private String cShopTypeName;
    private String bUse;
    private String cProvince;
    private String cCity;
    private String cCounty;
    private String cAddress;
    private String cSendName;
    private String iDot;
    private String iInvDot;

    public void setCID(String cID) {
        this.cID = cID;
    }

    public String getCID() {
        return cID;
    }

    public void setCUserAccountID(String cUserAccountID) {
        this.cUserAccountID = cUserAccountID;
    }

    public String getCUserAccountID() {
        return cUserAccountID;
    }

    public void setCSaleClassName(String cSaleClassName) {
        this.cSaleClassName = cSaleClassName;
    }

    public String getCSaleClassName() {
        return cSaleClassName;
    }

    public void setCGuestCode(String cGuestCode) {
        this.cGuestCode = cGuestCode;
    }

    public String getCGuestCode() {
        return cGuestCode;
    }

    public void setCGuestName(String cGuestName) {
        this.cGuestName = cGuestName;
    }

    public String getCGuestName() {
        return cGuestName;
    }

    public void setCGuestElseCode(String cGuestElseCode) {
        this.cGuestElseCode = cGuestElseCode;
    }

    public String getCGuestElseCode() {
        return cGuestElseCode;
    }

    public void setCChannelName(String cChannelName) {
        this.cChannelName = cChannelName;
    }

    public String getCChannelName() {
        return cChannelName;
    }

    public void setCChainName(String cChainName) {
        this.cChainName = cChainName;
    }

    public String getCChainName() {
        return cChainName;
    }

    public void setCShopTypeName(String cShopTypeName) {
        this.cShopTypeName = cShopTypeName;
    }

    public String getCShopTypeName() {
        return cShopTypeName;
    }

    public void setBUse(String bUse) {
        this.bUse = bUse;
    }

    public String getBUse() {
        return bUse;
    }

    public void setCProvince(String cProvince) {
        this.cProvince = cProvince;
    }

    public String getCProvince() {
        return cProvince;
    }

    public void setCCity(String cCity) {
        this.cCity = cCity;
    }

    public String getCCity() {
        return cCity;
    }

    public void setCCounty(String cCounty) {
        this.cCounty = cCounty;
    }

    public String getCCounty() {
        return cCounty;
    }

    public void setCAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public String getCAddress() {
        return cAddress;
    }

    public void setCSendName(String cSendName) {
        this.cSendName = cSendName;
    }

    public String getCSendName() {
        return cSendName;
    }

    public void setIDot(String iDot) {
        this.iDot = iDot;
    }

    public String getIDot() {
        return iDot;
    }

    public void setIInvDot(String iInvDot) {
        this.iInvDot = iInvDot;
    }

    public String getIInvDot() {
        return iInvDot;
    }

    @Override
    public String toString() {
        return "DealerNet{" +
                "cID='" + cID + '\'' +
                ", cUserAccountID='" + cUserAccountID + '\'' +
                ", cSaleClassName='" + cSaleClassName + '\'' +
                ", cGuestCode='" + cGuestCode + '\'' +
                ", cGuestName='" + cGuestName + '\'' +
                ", cGuestElseCode='" + cGuestElseCode + '\'' +
                ", cChannelName='" + cChannelName + '\'' +
                ", cChainName='" + cChainName + '\'' +
                ", cShopTypeName='" + cShopTypeName + '\'' +
                ", bUse='" + bUse + '\'' +
                ", cProvince='" + cProvince + '\'' +
                ", cCity='" + cCity + '\'' +
                ", cCounty='" + cCounty + '\'' +
                ", cAddress='" + cAddress + '\'' +
                ", cSendName='" + cSendName + '\'' +
                ", iDot='" + iDot + '\'' +
                ", iInvDot='" + iInvDot + '\'' +
                '}';
    }
}
