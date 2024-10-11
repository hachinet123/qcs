package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.naming.ldap.PagedResultsControl;
import java.time.LocalDate;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("centralkitchen.tr_bacteriacheck")
public class TrBacteriaCheck extends BaseEntity {

    /**
     *管理番号
     */
    @TableField("id")
    private Integer id;
    /**
     *タイトル
     */
    @TableField("title")
    private String title;
    /**
     *レシピID
     */
    @TableField("recipe_id")
    private  Integer recipeId;
    /**
     *菌検査状態区分ID
     */
    @TableField("checkstat_typeid")
    private  Integer checkStatTypeId;
    /**
     *	プロセスセンターID
     */
    @TableField("center_id")
    private Integer centerId;
    /**
     *他センター
     */
    @TableField("othercenter")
    private String otherCenter;
    /**
     *依頼者ID
     */
    @TableField("req_user_id")
    private Integer reqUserId;
    /**
     *依頼日
     */
    @TableField("req_date")
    private LocalDate reqDate;
    /**
     * 備考
     */
    @TableField("memo")
    private String memo;
    /***
     * 検査結果コメント
     */
    @TableField("comment")
    private String comment;

}
