package com.tre.centralkitchen.domain.vo.system;

import com.tre.centralkitchen.common.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 生産指示書自動印刷設定
 *
 * @author 10225441
 * @date 2023-12-19 13:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MtVariousAutoPrintVO extends BaseEntity {

    /**
     * プロセスセンターID
     */
    @ApiModelProperty("プロセスセンターID")
    private Integer centerId;

    /**
     * プロセスセンター
     */
    @ApiModelProperty("プロセスセンター")
    private String centerName;

    /**
     * 区別
     */
    @ApiModelProperty("区別")
    private Integer autoPrintTypeId;

    /**
     * ラインID
     */
    @ApiModelProperty("ラインID")
    private Integer lineId;

    /**
     * 印刷部数
     */
    @ApiModelProperty("印刷部数")
    private Integer qy;
}