package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.TableField;
import com.itextpdf.layout.property.TextAlignment;
import com.tre.centralkitchen.common.annotation.PdfTableProp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 10225441
 * @version 10116842
 */
@Data
public class ProductionInstructionVo {
    @PdfTableProp(title = "No.", width = 4.8f)
    @TableField(exist = false)
    private String no;
    @PdfTableProp(title = "", width = 2.5f, textAlign = TextAlignment.RIGHT, isBold = true)
    @TableField(exist = false)
    private String checkbox;
    private String id;

    @ApiModelProperty("センターコード")
    private Integer centerId;
    @Alias("生産日")
    @ApiModelProperty("生産日")
    private String scheduleDate;
    @Alias("便")
    @ApiModelProperty("便")
    private String mailNo;

    @ApiModelProperty("ラインコード")
    private Integer lineId;
    @Alias("ライン")
    @ApiModelProperty("ライン")
    private String lineName;
    @ApiModelProperty("作業グループId")
    private Integer workGroupId;
    @Alias("作業グループ")
    @ApiModelProperty("作業グループ")
    private String workGroupName;
    @PdfTableProp(title = "品番", width = 8.0f, textAlign = TextAlignment.LEFT, isBold = true)
    @Alias("品番")
    @ApiModelProperty("品番")
    private String callCode;
    @PdfTableProp(title = "商品名", width = 27.3f, textAlign = TextAlignment.LEFT)
    @Alias("商品名")
    @ApiModelProperty("商品名")
    private String itemName;
    @PdfTableProp(title = "計画", width = 7.0f, textAlign = TextAlignment.RIGHT, isBold = true)
    @Alias("生産計画")
    @ApiModelProperty("生産計画")
    private Integer qty;
    @PdfTableProp(title = "計量", width = 7.3f, textAlign = TextAlignment.LEFT)
    @Alias("計量")
    @ApiModelProperty("計量")
    private String itemWeigh;
    @PdfTableProp(title = "規格", width = 8.3f, textAlign = TextAlignment.LEFT)
    @Alias("規格")
    @ApiModelProperty("規格")
    private String itemSpecs;
    @PdfTableProp(title = "内容量", width = 8.3f, textAlign = TextAlignment.LEFT)
    @Alias("内容量")
    @ApiModelProperty("内容量")
    private String itemContent;
    @PdfTableProp(title = "産地", width = 13.0f, textAlign = TextAlignment.LEFT)
    @Alias("産地名")
    @ApiModelProperty("産地名")
    private String itemPlace;
    @PdfTableProp(title = "ロットGP", width = 13.3f, textAlign = TextAlignment.LEFT)
    @Alias("ロットGP")
    @ApiModelProperty("ロットGP")
    private String batchGroupName;

    private Integer mailId;
}
