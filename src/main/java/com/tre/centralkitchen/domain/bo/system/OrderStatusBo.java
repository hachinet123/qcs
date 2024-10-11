package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("OrderStatusBo")
public class OrderStatusBo extends BaseEntityBo {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "センターコード", required = true)
    @NotNull(message = "センターコードは必要フィールド！")
    private Integer centerId;

    @ApiModelProperty(value = "センター名", required = true)
    private String centerName;

    @ApiModelProperty("ベンダーコード")
    private Integer vendorId;

    @ApiModelProperty("便")
    private String mailNosCondition;

    private Integer[] mailNos;

    @ApiModelProperty(value = "納品日付範囲の開始日", required = true)
    @NotNull(message = "納品日付範囲の開始日は必要フィールド")
    private String deliveryStartDate;

    @ApiModelProperty(value = "納品日付範囲の終了日", required = true)
    @NotNull(message = "納品日付範囲の終了日は必要フィールド")
    private String deliveryEndDate;

    @ApiModelProperty("JAN")
    private String jan;

    @ApiModelProperty("呼出品番")
    private Integer callCd;

    @ApiModelProperty("商品名")
    private String productName;

    @ApiModelProperty("ラインCD")
    private String lineIdsCondition;

    private Integer[] lineIds;

    @ApiModelProperty("作業グループCD")
    private String workGroupIdsCondition;

    private Integer[] workGroupIds;

    @ApiModelProperty(value = "1:商品別、2:便別、3:店舗別", required = true)
    @NotNull(message = "displayFormatは必要フィールド！")
    @Range(min = 1, max = 3, message = "このフラグは1または3のみです")
    private Integer displayFormat;

}
