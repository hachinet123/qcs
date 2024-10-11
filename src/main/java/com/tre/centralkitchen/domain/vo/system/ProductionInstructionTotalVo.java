package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.tre.centralkitchen.common.annotation.PdfTableProp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 10225441
 * @version 10116842
 */
@Data
public class ProductionInstructionTotalVo {
    private String id;
    private String totalCount;
    @Alias("生産日")
    @ApiModelProperty("生産日")
    private String scheduleDate;
    @ApiModelProperty("ラインコード")
    private Integer lineId;
    @Alias("ライン")
    @ApiModelProperty("ライン")
    private String lineName;

    @ApiModelProperty("作業グループId")
    private Integer workGroupId;

    @PdfTableProp(title = "作業グループ", width = 30f, fontSize = 12f)
    @Alias("作業グループ")
    @ApiModelProperty("作業グループ")
    private String workGroupName;
    @PdfTableProp(title = "便", width = 10f, fontSize = 12f)
    @Alias("便")
    @ApiModelProperty("便")
    private String mailNo;
    @PdfTableProp(title = "合計", width = 30f, fontSize = 12f)
    @Alias("生産計画")
    @ApiModelProperty("生産計画")
    private String qty;
}
