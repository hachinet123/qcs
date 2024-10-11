package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 商品(加工品)センター別属性マスタ
 *
 * @author JDev
 * @since 2023-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExaminationSearchBo extends BaseEntityBo {

    @ApiModelProperty(value = "センター", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;

    @ApiModelProperty(value = "JAN")
    private String itemIds;

    @ApiModelProperty(value = "JAN")
    private String itemId;

    private List<String> itemIdList;

    @ApiModelProperty(value = "ライン", required = true)
    @NotNull(message = SysConstantInfo.LINE_NOT_EMPTY)
    private String lineId;

    private List<String> lineIdList;

    @ApiModelProperty(value = "呼出品番")
    private Integer callCode;

    @ApiModelProperty(value = "商品名")
    private String itemNames;

    private List<String> itemNameList;

    public void build() {
        if (this.lineId != null) {
            setLineIdList(Arrays.asList(this.lineId.split(",")));
        }
        if (this.itemIds != null) {
            setItemIdList(Arrays.asList(this.itemIds.split(",")));
        }
        if (this.itemNames != null) {
            setItemNameList(Arrays.asList(this.itemNames.split(",")));
        }
    }
}
