package com.tre.centralkitchen.domain.bo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author 10225441
 */
@Data
@ApiModel(value = "生産指示書")
public class ProductionInstructionBo extends BaseEntityBo {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "センター", example = "32", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;
    @ApiModelProperty(value = "便", example = "1,2,3")
    private String mailNo;
    @ApiModelProperty(value = "ライン", example = "1,2,3")
    @NotBlank(message = SysConstantInfo.LINE_NOT_EMPTY)
    private String lineId;
    @ApiModelProperty(value = "作業グループ", example = "1,2,3")
    private String workGroupId;
    @ApiModelProperty(value = "クエリ開始日", example = "20221201", required = true)
    @NotBlank(message = SysConstantInfo.STR_DATE_NOT_EMPTY)
    @JsonFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @Pattern(regexp = FormatConstants.REGEX_PATTERN_DATE_WITH_SLASH, message = SysConstantInfo.STR_DATE_CHECK)
    private String stDate;
    @ApiModelProperty(value = "クエリ終了日", example = "20221231", required = true)
    @NotBlank(message = SysConstantInfo.END_DATE_NOT_EMPTY)
    @JsonFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @Pattern(regexp = FormatConstants.REGEX_PATTERN_DATE_WITH_SLASH, message = SysConstantInfo.END_DATE_CHECK)
    private String edDate;
    @ApiModelProperty(value = "日付タイプ", example = "0", required = true)
    @NotNull(message = SysConstantInfo.DATE_TYPE_NOT_EMPTY1)
    @Max(value = 1, message = "日付タイプ" + SysConstantInfo.NUMERICAL_LIMIT)
    @Min(value = 0, message = "日付タイプ" + SysConstantInfo.NUMERICAL_LIMIT)
    private Integer dateType;

    @ApiModelProperty(value = "生産計画数を合計する", example = "0")
    private Integer totalFlag;

    /* The following fields need to be converted because their data types are inconsistent with those stored in the database. */

    @ApiModelProperty(hidden = true)
    private List<Integer> lineIdList;
    @ApiModelProperty(hidden = true)
    private List<Short> mailIdList;
    @ApiModelProperty(hidden = true)
    private List<Short> workgroupIdList;
    @ApiModelProperty(hidden = true)
    private Boolean mailNoAllFlag;
}
