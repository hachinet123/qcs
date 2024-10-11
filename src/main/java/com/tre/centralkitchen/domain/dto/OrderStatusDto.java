package com.tre.centralkitchen.domain.dto;

import com.tre.centralkitchen.domain.vo.system.OrderStatusMailControlVo;
import com.tre.centralkitchen.domain.vo.system.OrderStatusWorkGroupVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Order status bo
 *
 * @date 2022-11-30
 */
@Data
@ApiModel("OrderStatusBo")
public class OrderStatusDto implements Serializable {

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

    private Integer pageSize;
    private Integer pageNum;

    private List<OrderStatusMailControlVo> mailControlList;

    private List<OrderStatusWorkGroupVo> workGroupList;


    public void setMailNosCondition(String mailNosCondition) {
        this.mailNosCondition = mailNosCondition;
        this.mailNos = toIntegerArray(mailNosCondition);
    }

    public void setLineIdsCondition(String lineIdsCondition) {
        this.lineIdsCondition = lineIdsCondition;
        this.lineIds = toIntegerArray(lineIdsCondition);

    }

    public void setWorkGroupIdsCondition(String workGroupIdsCondition) {
        this.workGroupIdsCondition = workGroupIdsCondition;
        this.workGroupIds = toIntegerArray(workGroupIdsCondition);

    }

    private Integer[] toIntegerArray(String numbersString) {
        if (null == numbersString || numbersString.trim().equals("")) {
            return new Integer[0];
        }
        String[] numbers = numbersString.split(",");
        Integer[] result = new Integer[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            result[i] = Integer.valueOf(numbers[i]);
        }
        return result;
    }


}
