package com.tre.centralkitchen.domain.vo.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BranchListVo {
    /**
     * 拠点コード
     */
    @ApiModelProperty("拠点コード")
    private Integer branchcd;

    /**
     * 拠点名
     */
    @ApiModelProperty("拠点名")
    private String branchname;

    /**
     * 拠点名称カナ
     */
    @ApiModelProperty("拠点名称カナ")
    private String branchnameRead;

    /**
     * 拠点略称
     */
    @ApiModelProperty("拠点略称")
    private String branchnameAbbr;
}
