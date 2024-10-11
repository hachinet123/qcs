package com.tre.centralkitchen.domain.bo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class DocumentLinkSearchBo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "センター", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private String centerId;

    @ApiModelProperty(value = "本日", required = true)
    @NotNull(message = "区分を選択してください。")
    private Integer dateType;

    @ApiModelProperty(value = "日付開始日", example = "2022/12/01", required = true)
    @NotBlank(message = SysConstantInfo.STR_DATE_NOT_EMPTY)
    @JsonFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @Pattern(regexp = FormatConstants.REGEX_PATTERN_DATE_WITH_SLASH, message = SysConstantInfo.STR_DATE_CHECK)
    private String stDate;

    @ApiModelProperty(value = "日付終了日", example = "2022/12/31", required = true)
    @NotBlank(message = SysConstantInfo.END_DATE_NOT_EMPTY)
    @JsonFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @Pattern(regexp = FormatConstants.REGEX_PATTERN_DATE_WITH_SLASH, message = SysConstantInfo.END_DATE_CHECK)
    private String edDate;

    public void build() {
        this.stDate = this.stDate + " 00:00:00";
        this.edDate = this.edDate + " 23:59:59";
    }
}
