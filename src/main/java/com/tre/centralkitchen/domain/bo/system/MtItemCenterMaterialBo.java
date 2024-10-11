package com.tre.centralkitchen.domain.bo.system;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class MtItemCenterMaterialBo extends BaseEntityBo {

    @ExcelProperty(value = "プロセスセンターID", index = 0)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @ApiModelProperty(value = "プロセスセンターID")
    private Integer centerId;

    @ExcelProperty(value = "原材料JAN", index = 1)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @ApiModelProperty(value = "原材料JAN" )
    private String itemId;

    @ExcelProperty(value = "安全在庫", index = 2)
    @ContentStyle(locked = BooleanEnum.FALSE)
    @ApiModelProperty(value = "安全在庫" )
    private Integer safetyStockQy;

    @ExcelProperty(value = "エラーメッセージ", index = 3)
    @ContentStyle(locked = BooleanEnum.FALSE)
    private String errorMsg;

}
