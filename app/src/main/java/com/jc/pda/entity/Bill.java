package com.jc.pda.entity;

/**
 * Created by z on 2018/1/2.
 */

public class Bill {
    private String billId;
    private String productId;
    private String batchId;
    private String wholesaleId;
    private String dealerId;
    private String shopId;
    /**
     * 0    入库
     * 1    出库
     * 2    退货
     * 3    出入库
     */
    private int billStyle;
    /**
     * 0    首次上传
     * 1    重复上传
     */
    private int upType;
    private boolean isUp;
    private String oldBillNo;
    private String adminId;
    private String upDateString;
    private long upDateInt;
    private boolean isDelete;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getWholesaleId() {
        return wholesaleId;
    }

    public void setWholesaleId(String wholesaleId) {
        this.wholesaleId = wholesaleId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getBillStyle() {
        return billStyle;
    }

    public void setBillStyle(int billStyle) {
        this.billStyle = billStyle;
    }

    public int getUpType() {
        return upType;
    }

    public void setUpType(int upType) {
        this.upType = upType;
    }

    public boolean isUp() {
        return isUp;
    }

    public void setUp(boolean up) {
        isUp = up;
    }

    public String getOldBillNo() {
        return oldBillNo;
    }

    public void setOldBillNo(String oldBillNo) {
        this.oldBillNo = oldBillNo;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUpDateString() {
        return upDateString;
    }

    public void setUpDateString(String upDateString) {
        this.upDateString = upDateString;
    }

    public long getUpDateInt() {
        return upDateInt;
    }

    public void setUpDateInt(long upDateInt) {
        this.upDateInt = upDateInt;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId='" + billId + '\'' +
                ", productId='" + productId + '\'' +
                ", batchId='" + batchId + '\'' +
                ", wholesaleId='" + wholesaleId + '\'' +
                ", dealerId='" + dealerId + '\'' +
                ", shopId='" + shopId + '\'' +
                ", billStyle=" + billStyle +
                ", upType=" + upType +
                ", isUp=" + isUp +
                ", oldBillNo='" + oldBillNo + '\'' +
                ", adminId='" + adminId + '\'' +
                ", upDateString='" + upDateString + '\'' +
                ", upDateInt=" + upDateInt +
                ", isDelete=" + isDelete +
                '}';
    }
}
