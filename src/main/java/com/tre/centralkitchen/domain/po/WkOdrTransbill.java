package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.wk_odr_transbill")
public class WkOdrTransbill extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * プロセスセンターID
     */
    @TableField("center_id")
    private Integer centerId;

    /**
     * 振替日
     */
    @TableField("trans_date")
    private Date transDate;

    /**
     * 伝票ID
     */
    @TableField("transbill_id")
    private Integer transbillId;

    /**
     * 伝票履歴先頭ID
     */
    @TableField("id_1st")
    private Integer id1st;

    /**
     * 伝票区分ID
     */
    @TableField("bill_kindid")
    private Integer billKindid;

    /**
     * 伝票番号
     */
    @TableField("transbill_code")
    private Integer transbillCode;

    /**
     * 振替元センター店舗ID
     */
    @TableField("out_org_id")
    private Integer outOrgId;

    /**
     * 振替元部門コード
     */
    @TableField("out_department_id")
    private Integer outDepartmentId;

    /**
     * 振替先店舗ID
     */
    @TableField("in_org_id")
    private Integer inOrgId;

    /**
     * 振替先部門コード
     */
    @TableField("in_department_id")
    private Integer inDepartmentId;

    /**
     * 振替伝票ID管理区分ID
     */
    @TableField("itemid_typeid")
    private Integer itemidTypeid;

    /**
     * 振替出荷番号
     */
    @TableField("o_transbill_id")
    private String oTransbillId;

    /**
     * 仕入親伝番号
     */
    @TableField("dlvbill_id")
    private String dlvbillId;

    /**
     * 取引先コード
     */
    @TableField("vendor_id")
    private Integer vendorId;

    /**
     * 伝票日付
     */
    @TableField("bill_date")
    private Date billDate;

    /**
     * 便
     */
    @TableField("mail_no")
    private Integer mailNo;

    /**
     * 入庫日
     */
    @TableField("in_date")
    private Date inDate;

    /**
     * 計上日
     */
    @TableField("count_date")
    private Date countDate;

    /**
     * 納品経路区分ID
     */
    @TableField("dlvroute_typeid")
    private Integer dlvrouteTypeid;

    /**
     * 通過在庫区分ID
     */
    @TableField("tcdc_typeid")
    private Integer tcdcTypeid;

    /**
     * 納品形式区分ID
     */
    @TableField("dlvway_typeid")
    private Integer dlvwayTypeid;

    /**
     * 物流タイプ
     */
    @TableField("shipping_typeid")
    private Integer shippingTypeid;

    /**
     * 温度帯区分ID
     */
    @TableField("thermal_zone_typeid")
    private Integer thermalZoneTypeid;

    /**
     * 合計原価金額
     */
    @TableField("o_am")
    private Long oAm;

    /**
     * 合計売価金額
     */
    @TableField("s_am")
    private Long sAm;

    /**
     * 行番号
     */
    @TableField("line_no")
    private Integer lineNo;

    /**
     * 数量_1000倍小数
     */
    @TableField("qy")
    private Integer qy;

    /**
     * 原単価_1000倍小数
     */
    @TableField("cost")
    private Integer cost;

    /**
     * 原価金額
     */
    @TableField("order_am")
    private Long orderAm;

    /**
     * 売価金額
     */
    @TableField("sales_am")
    private Long salesAm;

    /**
     * 売単価
     */
    @TableField("price")
    private Integer price;

    /**
     * JAN
     */
    @TableField("item_id")
    private String itemId;

    /**
     * 勘定科目ID
     */
    @TableField("accitem_id")
    private Integer accitemId;

    /**
     * 振替先商品コード
     */
    @TableField("in_item_id")
    private String inItemId;

    /**
     * 理由区分ID
     */
    @TableField("reason_typeid")
    private Integer reasonTypeid;

    /**
     * 振替伝票-仕入伝票紐付情報_伝票情報
     */
    @TableField("transbill_id_1st")
    private Integer transbillId1st;

    /**
     * 振替伝票-仕入伝票紐付情報_明細情報
     */
    @TableField("transitem_line_no")
    private Integer transitemLineNo;

    /**
     * 転送日時
     */
    @TableField("send_date")
    private Date sendDate;

    /**
     * 転送フラグ
     */
    @TableField("f_send")
    private Integer fSend;

}
