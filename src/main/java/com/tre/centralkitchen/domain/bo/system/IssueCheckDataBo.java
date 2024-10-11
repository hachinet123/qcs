package com.tre.centralkitchen.domain.bo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class IssueCheckDataBo extends BaseEntityBo {
    /**
     * 店舗Id
     */
    @ApiModelProperty(value = "店舗Id", required = true)
    private String storeId;
    /**
     * JAN
     */
    @ApiModelProperty(value = "JAN")
    @Size(max = 13, message = SysConstantInfo.JAN_MAX)
    private String itemId;
    /**
     * 品番
     */
    @ApiModelProperty(value = "品番")
    @Size(max = 6, message = SysConstantInfo.CALL_CODE_MAX)
    private String callCode;
    /**
     * 商品名
     */
    @ApiModelProperty(value = "商品名")
    @Size(max = 50, message = SysConstantInfo.ITEM_NAME_MAX)
    private String itemName;
    /**
     * ライン
     */
    @ApiModelProperty(value = "ライン")
    private String lineId;
    @ApiModelProperty(value = "開始日", example = "2022/12/31", required = true)
    @NotBlank(message = SysConstantInfo.STR_DATE_NOT_EMPTY)
    @JsonFormat(pattern = "yyyy/MM/dd", shape = JsonFormat.Shape.STRING)
    @Pattern(regexp = FormatConstants.REGEX_PATTERN_DATE_WITH_SLASH, message = SysConstantInfo.STR_DATE_CHECK)
    private String startDate;
    @ApiModelProperty(value = "終了日", example = "2022/12/31", required = true)
    @NotBlank(message = SysConstantInfo.END_DATE_NOT_EMPTY)
    @JsonFormat(pattern = "yyyy/MM/dd", shape = JsonFormat.Shape.STRING)
    @Pattern(regexp = FormatConstants.REGEX_PATTERN_DATE_WITH_SLASH, message = SysConstantInfo.END_DATE_CHECK)
    private String endDate;
    private List<String> storeIdList;
    private List<String> lineIdList;

}
