package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 10253955
 * @since 2023-12-11 16:56
 */
@Data
@ApiModel("LabelCheckBo")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LabelCheckBo {

    @ApiModelProperty(value = "センターコード", required = true)
    @NotNull(message = "センターコードは必要フィールド！")
    private Integer centerId;

    @ApiModelProperty(value = "ライン", required = true, example = "1,2,3")
    @NotNull(message = "ラインは必要フィールド！")
    private Integer[] lineId;

    @ApiModelProperty(value = "JAN", example = "1,2,3")
    private String[] jan;

    @ApiModelProperty(value = "品番", example = "1,2,3")
    private Integer[] callCode;

    @ApiModelProperty(value = "商品名", example = "1,2,3")
    private String[] itemName;

    /**
     * 日付タイプ 0：生産日 1：納品予定日
     */
    @ApiModelProperty(value = "日付タイプ", example = "0", required = true)
    @NotNull(message = SysConstantInfo.DATE_TYPE_NOT_EMPTY1)
    @Max(value = 1, message = "日付タイプ" + SysConstantInfo.NUMERICAL_LIMIT)
    @Min(value = 0, message = "日付タイプ" + SysConstantInfo.NUMERICAL_LIMIT)
    private Integer dateType;

    /**
     * クエリ開始日
     */
    @ApiModelProperty(value = "クエリ開始日", example = "2022/12/01", required = true)
    @NotNull(message = SysConstantInfo.STR_DATE_NOT_EMPTY)
    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    private LocalDate deliveryStartDate;

    /**
     * クエリ終了日
     */
    @ApiModelProperty(value = "クエリ終了日", example = "2022/12/31", required = true)
    @NotNull(message = SysConstantInfo.END_DATE_NOT_EMPTY)
    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    private LocalDate deliveryEndDate;

    private List<Integer> mailNoList;
}
