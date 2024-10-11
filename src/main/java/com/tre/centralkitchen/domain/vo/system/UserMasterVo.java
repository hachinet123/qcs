package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserMasterVo {

    /**
     * 社員番号
     */
    @Alias("社員番号")
    @ApiModelProperty(value = "社員番号")
    private String userId;

    /**
     * 社員名
     */
    @Alias("社員名")
    @ApiModelProperty(value = "社員名")
    private String userName;

    /**
     * 所属センター
     */
    @ApiModelProperty(value = "所属センター")
    private String centerId;

    /**
     * 所属センター名
     */
    @Alias("所属センター名")
    @ApiModelProperty(value = "所属センター名")
    private String centerName;

    /**
     * 所属ライン
     */
    @ApiModelProperty(value = "所属ライン")
    private String lineId;

    /**
     * 所属ライン名
     */
    @Alias("所属ライン名")
    @ApiModelProperty(value = "所属ライン名")
    private String lineName;
    /**
     * 備考
     */
    @Alias("備考")
    @ApiModelProperty(value = "備考")
    private String memo;

}
