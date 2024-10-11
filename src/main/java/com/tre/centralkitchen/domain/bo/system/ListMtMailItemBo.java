package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("ListMtMailItemBo")
public class ListMtMailItemBo extends BaseEntityBo {
    @ApiModelProperty(value = "プロセスセンターID", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;
    /**
     * 便
     */
    @ApiModelProperty(value = "便番号")
    private String mailNo;
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
    @NotBlank(message = SysConstantInfo.LINE_NOT_EMPTY)
    private String lineId;
    private List<String> mailNoList;

    private List<String> linesList;
    private Integer pageSize;
    private Integer pageNum;
    private String centerName;
    private Integer vendorId;

}