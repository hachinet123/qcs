package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 便設定個別マスタ 検索
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("MtMailItemBo")
public class MtMailItemBo extends BaseEntityBo {
    /**
     * プロセスセンターID
     */
    @ApiModelProperty(value = "プロセスセンターID", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;
    /**
     * 便
     */
    @ApiModelProperty(value = "便番号")
    private String mailNo;
    /**
     * 昔の便り
     */
    @ApiModelProperty(value = "昔の便り")
    private String beforeMailNo;
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
     * 店舗ID
     */
    @ApiModelProperty(value = "店舗ID")
    private Integer storeId;
    /**
     * 備考
     */
    @ApiModelProperty(value = "備考")
    @Size(max = 50, message = SysConstantInfo.MEMO_MAX)
    private String memo;
    private List<String> mailNoList;
}
