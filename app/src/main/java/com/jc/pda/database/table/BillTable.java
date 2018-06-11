package com.jc.pda.database.table;

/**
 * Created by z on 2017/12/22.
 */

public class BillTable {
    /**
     * 表名字
     */
    public static final String TABLE = "Bill";
    /**
     * 本地id
     */
    public static final String _id = "_id";
    /**
     * 单据id
     */
    public static final String BillId = "BillId";
    /**
     * 产品Id
     */
    public static final String GoodId = "GoodId";
    /**
     * 批号Id
     */
    public static final String BatchId = "BatchId";
    /**
     * 批发商Id
     */
    public static final String WholesaleId = "WholesaleId";
    /**
     * 经销商Id
     */
    public static final String DealerId = "DealerId";
    /**
     * 门店Id
     */
    public static final String ShopId = "ShopId";
    /**
     * 条码类型 入库 出库 退货 出入库
     */
    public static final String BillStyle = "BillStyle";
    /**
     * 首次上传 重复上传
     */
    public static final String UpType = "UpType";
    /**
     * 是否上传
     */
    public static final String isUp = "isUp";
    /**
     * 首次上传 重复上传
     */
    public static final String OldBillNo = "OldBillNo";
    /**
     * 操作人ID
     */
    public static final String AdminId = "AdminId";
    /**
     *  更新日期字符串
     */
    public static final String UpDateString = "UpDateString";
    /**
     *  更新日期int值 毫秒数
     */
    public static final String UpDateInt = "UpDateInt";
    /**
     *  是否删除
     */
    public static final String IsDelete = "IsDelete";
    /**
     *  保留字段1
     */
    public static final String NoteA = "NoteA";
    /**
     *  保留字段2
     */
    public static final String NoteB = "NoteB";
    /**
     *  保留字段3
     */
    public static final String NoteC = "NoteC";

}
