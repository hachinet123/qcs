package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkGroupVo extends BaseEntityVo {
    private static final long serialVersionUID = 1L;

    /**
     * センターID
     */
    @ApiModelProperty(value = "センターID")
    private Integer centerId;
    /**
     * センター名
     */
    @ApiModelProperty(value = "センター名")
    @Alias("センター")
    private String centerName;
    /**
     * ラインID
     */
    @ApiModelProperty(value = "ラインID")
    private Integer lineId;
    /**
     * ライン名
     */
    @ApiModelProperty(value = "ライン名")
    @Alias("ライン")
    private String lineName;
    /**
     * 大分類
     */
    @ApiModelProperty(value = "大分類")
    private Integer groupBig;
    /**
     * 大分類名
     */
    @ApiModelProperty(value = "大分類名")
    @Alias("作業グループ-大分類")
    private String groupBigName;
    /**
     * 小分類
     */
    @ApiModelProperty(value = "小分類")
    private Integer groupSmall;
    /**
     * 小分類名
     */
    @ApiModelProperty(value = "小分類名")
    @Alias("作業グループ-小分類")
    private String groupSmallName;

}
