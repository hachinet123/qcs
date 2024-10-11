package com.tre.centralkitchen.domain.vo.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import lombok.Data;

import java.util.Date;

@Data
public class MtMailItemHistoryVo extends BaseEntityVo {

    private static final long serialVersionUID = 1L;
    /**
     * バックアップ日時
     */
    private Date backupDate;
    /**
     * プロセスセンターID
     */
    private Integer centerId;

    /**
     * JAN
     */
    private String itemId;
    /**
     * 店舗ID
     */
    private Integer storeId;
    /**
     * 便番号
     */
    private Integer mailNo;

    /**
     * 備考
     */
    private String memo;
    /**
     * 修正フラグ(1:新規2:更新3:削除)
     */
    private Integer flag;
}
