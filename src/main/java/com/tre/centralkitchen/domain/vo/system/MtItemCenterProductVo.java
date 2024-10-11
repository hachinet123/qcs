package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.alibaba.excel.annotation.ExcelProperty;
import com.tre.centralkitchen.common.domain.BaseEntityVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 検食不要一覧
 *
 * @author JDev
 * @since 2023-12-12
 */
@Data
public class MtItemCenterProductVo {
    private static final long serialVersionUID = 1L;
    /**
     * プロセスセンターID
     */
    @ApiModelProperty("センター")
    private Integer centerId;

    /**
     * プロセスセンター名
     */
    @Alias("センター")
    @ApiModelProperty("センター")
    private String centerName;

    @ApiModelProperty(value = "ライン")
    private Integer lineId;

    @Alias("ライン")
    @ApiModelProperty(value = "ライン名")
    private String lineName;

    /**
     * 商品JAN
     */
    @Alias("JAN")
    @ApiModelProperty("商品JAN")
    private String itemId;

    @Alias("品番")
    @ApiModelProperty(value = "呼出品番")
    private Integer callCode;

    @Alias("商品名")
    @ApiModelProperty(value = "商品名")
    private String itemName;

    /**
     * 加工賃
     */
    @ApiModelProperty("加工賃")
    private BigDecimal pcFee;

    /**
     * 作業グループID
     */
    @ApiModelProperty("作業グループID")
    private Integer wkgrpId;

    /**
     * コンテナ入数
     */
    @ApiModelProperty("コンテナ入数")
    private Integer containerUnit;

    /**
     * 検食数
     */
    @ApiModelProperty("検食数")
    private Integer tasteQy;

    /**
     * 製造時生産性
     */
    @ApiModelProperty("製造時生産性")
    private Integer productivity;

    /**
     * 製造時ラインスピード
     */
    @ApiModelProperty("製造時ラインスピード")
    private Integer lineSpeed;

    /**
     * 備考
     */
    @ApiModelProperty("備考")
    private String memo;
}
