package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class MtMenuVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * メニューID
     */
    @ApiModelProperty("メニューID")
    private Integer id;

    /**
     * メニュー表示順
     */
    @ApiModelProperty("メニューID")
    private String name;

    /**
     * メニューID（親）
     */
    @ApiModelProperty("メニューID（親）")
    private Integer pid;

    /**
     * ソート
     */
    @ApiModelProperty("ソート")
    private Integer seq;

    /**
     * 画面遷移URL
     */
    @ApiModelProperty("画面遷移URL")
    private String openUrl;

    /**
     * 画面新規オープン区分ID(1:同一Tabで開く/2:新規Tabで開く/3:新規Windowで開く)
     */
    @ApiModelProperty("画面新規オープン区分ID")
    private Integer openTypeid;

    /**
     * 画面ルーターパス
     */
    @ApiModelProperty("画面ルーターパス")
    private String routerPath;

    /**
     * アイコン名
     */
    @ApiModelProperty("アイコン名")
    private String icon;

    /**
     * 備考
     */
    @ApiModelProperty("備考")
    private String memo;

    @ApiModelProperty("0:表示,1:非表示")
    private Integer fShow;

}
