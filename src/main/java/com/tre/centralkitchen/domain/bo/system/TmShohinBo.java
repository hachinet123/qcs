package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Data
@ApiModel(value = "生産指示書")
public class TmShohinBo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "センターコード", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;

    @ApiModelProperty(value = "呼出品番")
    private Integer callCode;

    @ApiModelProperty(value = "JAN")
    private String itemIds;

    private List<String> itemIdList;

    @ApiModelProperty(value = "商品名")
    private String itemNames;

    private List<String> itemNameList;

    @ApiModelProperty(value = "ライン")
    @NotNull(message = SysConstantInfo.LINE_NOT_EMPTY)
    private String lineId;

    private List<String> lineIdList;

    @ApiModelProperty(value = "廃盤フラグ")
    private Boolean fActive;

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
