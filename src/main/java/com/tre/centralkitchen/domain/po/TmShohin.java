package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author JDev
 * @since 2023-01-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.tm_shohin")
public class TmShohin extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 呼出品番
     */
    private Integer callCode;

    /**
     * ID
     */
    private Integer id;

    /**
     * 地域ID
     */
    private Integer areaId;

    /**
     * 店舗ID
     */
    private Integer storeId;

    /**
     * JAN
     */
    private String itemId;

    /**
     * 商品名
     */
    private String itemName;

    /**
     * 商品1段目
     */
    @TableField("item_name_1dan")
    private String itemName1dan;

    /**
     * 商品2段目
     */
    @TableField("item_name_2dan")
    private String itemName2dan;

    /**
     * 商品名カナ
     */
    @TableField("item_name_k")
    private String itemNameK;

    /**
     * 規格分量
     */
    private Integer nSzname;

    /**
     * 規格単位CD
     */
    private Integer nSznameTypeid;

    /**
     * 標準内容量
     */
    private Integer volume;

    /**
     * 標準内容量単位CD
     */
    private Integer volumeTypeid;

    /**
     * グループCD
     */
    private Integer lineId;

    /**
     * 作業グループCD
     */
    private Integer wkgrpId;

    /**
     * 部門CD
     */
    private Integer departmentId;

    /**
     * ミニ部門CD
     */
    private Integer categoryId;

    /**
     * 品種CD
     */
    private Integer subcategoryId;

    /**
     * 生鮮商品種別区分ID-1:商品/8:原材料/10:中間品
     */
    private Integer fritemTypeid;

    /**
     * 計量区分ID-0:不定貫/1:定貫/2:定貫(計量あり)
     */
    private Integer teikanTypeid;

    /**
     * 基準PC原単価
     */
    private BigDecimal costPc;

    /**
     * 基準店原単価
     */
    private BigDecimal cost;

    /**
     * 基準店売価
     */
    private BigDecimal price;

    /**
     * 賞味期間
     */
    private Integer exptime;

    /**
     * コンテナタイプ
     */
    private Integer containerId;

    /**
     * コンテナ入数
     */
    private Integer containerUnit;

    /**
     * ベンダーID
     */
    private Integer vendorId;

    /**
     * 高さ
     */
    private BigDecimal height;

    /**
     * 幅
     */
    private BigDecimal width;

    /**
     * 奥行
     */
    private BigDecimal depth;

    /**
     * 計量器対象フラグ
     */
    private Integer fWm;

    /**
     * バーコード区分
     */
    private Integer barcodeTypeid;

    /**
     * 産地ID
     */
    private Integer placeId;

    /**
     * 広告文ID
     */
    private Integer adId;

    /**
     * コメントID
     */
    private Integer commentId;

    /**
     * アレルギーID
     */
    private Integer alergyId;

    /**
     * ラベラー区分ID
     */
    private Integer labelerTypeid;

    /**
     * PC対象区分ID
     */
    private Integer pctgtTypeid;

    /**
     * 廃盤フラグ-1:販売中/9:廃盤
     */
    private Integer fActive;

    /**
     * ロットグループID
     */
    private Integer lotgrpId;

    /**
     * 風袋重量(g)
     */
    private Integer tareWeight;

    /**
     * 上限重量(g)
     */
    private BigDecimal maxWeight;

    /**
     * 下限重量(g)
     */
    private Integer minWeight;

    /**
     * カロリー
     */
    private Integer calory;

    /**
     * 二枚下貼区分
     */
    private Integer labelputTypeid;

}