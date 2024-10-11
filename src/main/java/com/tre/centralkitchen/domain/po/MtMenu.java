package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "centralkitchen.mt_menu")
public class MtMenu extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * メニューID
     */
    private Integer id;

    /**
     * メニュー表示順
     */
    private String name;

    /**
     * メニューID（親）
     */
    private Integer pid;

    /**
     * ソート
     */
    private Integer seq;

    /**
     * 画面遷移URL
     */
    private String openUrl;

    /**
     * 画面新規オープン区分ID(1:同一Tabで開く/2:新規Tabで開く/3:新規Windowで開く)
     */
    private Integer openTypeid;

    /**
     * 画面ルーターパス
     */
    private String routerPath;

    /**
     * アイコン名
     */
    private String icon;

    /**
     * 備考
     */
    private String memo;

    /**
     * 0:表示,1:非表示
     */
    @TableField("f_show")
    private Integer fShow;

}
