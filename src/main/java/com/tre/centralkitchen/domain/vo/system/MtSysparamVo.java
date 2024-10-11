package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * システム設定
 * </p>
 *
 * @author JDev
 * @since 2022-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MtSysparamVo {

    /**
     * システムID
     */
    @ApiModelProperty("システムID")
    private Integer systemId;

    /**
     * パラメーターID
     */
    @ApiModelProperty("パラメーターID")
    private Integer paramId;

    /**
     * パラメーター値1
     */
    @ApiModelProperty("パラメーター値1")
    private String paramVal1;

    /**
     * パラメーター値2
     */
    @ApiModelProperty("パラメーター値2")
    private String paramVal2;

    /**
     * パラメーター値3
     */
    @ApiModelProperty("パラメーター値3")
    private String paramVal3;

    /**
     * パラメーター値4
     */
    @ApiModelProperty("パラメーター値4")
    private String paramVal4;

    /**
     * パラメーター値5
     */
    @ApiModelProperty("パラメーター値5")
    private String paramVal5;

    /**
     * 備考
     */
    @ApiModelProperty("備考")
    private String memo;
}
