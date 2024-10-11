package com.tre.centralkitchen.domain.bo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "振分指示書")
public class DistributionInstructionBo extends BaseEntityBo {

    @ApiModelProperty(value = "センター", example = "32", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;

    @ApiModelProperty(value = "便", example = "1,2,3")
    private String mailNo;

    @ApiModelProperty(value = "ライン", example = "1,2,3")
    private String lineId;

    @ApiModelProperty(value = "作業グループ", example = "1,2,3")
    private String workGroupId;

    @ApiModelProperty(value = "クエリ開始日", example = "2022/12/01", required = true)
    @NotBlank(message = SysConstantInfo.STR_DATE_NOT_EMPTY)
    @JsonFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @Pattern(regexp = FormatConstants.REGEX_PATTERN_DATE_WITH_SLASH, message = SysConstantInfo.STR_DATE_CHECK)
    private String stDate;

    @ApiModelProperty(value = "クエリ終了日", example = "2022/12/31", required = true)
    @NotBlank(message = SysConstantInfo.END_DATE_NOT_EMPTY)
    @JsonFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @Pattern(regexp = FormatConstants.REGEX_PATTERN_DATE_WITH_SLASH, message = SysConstantInfo.END_DATE_CHECK)
    private String edDate;

    @ApiModelProperty(value = "日付タイプ", example = "0", required = true)
    @NotNull(message = SysConstantInfo.DATE_TYPE_NOT_EMPTY)
    @Max(value = 1, message = "日付タイプ" + SysConstantInfo.NUMERICAL_LIMIT)
    @Min(value = 0, message = "日付タイプ" + SysConstantInfo.NUMERICAL_LIMIT)
    private Integer dateType;

    @ApiModelProperty(value = "検索時数量ゼロ除く", example = "0")
    @NotNull(message = "検索時数量ゼロ除く入力してください")
    @Max(value = 1, message = "検索時数量ゼロ除く" + SysConstantInfo.NUMERICAL_LIMIT)
    @Min(value = 0, message = "検索時数量ゼロ除く" + SysConstantInfo.NUMERICAL_LIMIT)
    private Integer isExceptZero;

    @ApiModelProperty(value = "指示書印刷時閉店除く", example = "0")
    @NotNull(message = "指示書印刷時閉店除く入力してください")
    @Max(value = 1, message = "指示書印刷時閉店除く" + SysConstantInfo.NUMERICAL_LIMIT)
    @Min(value = 0, message = "指示書印刷時閉店除く" + SysConstantInfo.NUMERICAL_LIMIT)
    private Integer isClosed;

    @ApiModelProperty(hidden = true)
    private List<Integer> lineIdList;

    @ApiModelProperty(hidden = true)
    private List<Short> mailIdList;

    @ApiModelProperty(hidden = true)
    private List<Short> workgroupIdList;
}
