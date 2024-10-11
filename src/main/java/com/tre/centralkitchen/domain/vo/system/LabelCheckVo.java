package com.tre.centralkitchen.domain.vo.system;

import cn.hutool.core.annotation.Alias;
import com.itextpdf.layout.property.TextAlignment;
import com.tre.centralkitchen.common.annotation.PdfTableProp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * <p>
 *
 * </p>
 *
 * @author 10253955
 * @since 2023-12-11 16:50
 */
@Data
public class LabelCheckVo {

    /**
     * センター
     */
    @Alias("センター")
    @ApiModelProperty("センター")
    private String lineName;

    @Alias("checkBox")
    @ApiModelProperty("checkBox")
    @PdfTableProp(title = "", width = 2.0f, fontSize = 5f, textAlign = TextAlignment.CENTER)
    private String checkBox;

    /**
     * 品番
     */
    @Alias("品番")
    @ApiModelProperty("品番")
    @PdfTableProp(title = "品番", width = 6.0f, fontSize = 8f, textAlign = TextAlignment.LEFT)
    private Integer finalCallCode;

    /**
     * 商品名
     */
    @Alias("商品名")
    @ApiModelProperty("商品名")
    @PdfTableProp(title = "商品名", width = 20.3f, fontSize = 8f, textAlign = TextAlignment.LEFT)
    private String finalItemName;

    /**
     * 賞味期限
     */
    @Alias("賞味期限")
    @ApiModelProperty("賞味期限")
    @PdfTableProp(title = "賞味期限", width = 9.0f, fontSize = 8f, textAlign = TextAlignment.CENTER)
    private Integer finalExpTime;

    /**
     * 便　(1-25)
     */
    @Alias("便")
    @ApiModelProperty("便")
    private Integer mailNo;

    /**
     * 納品予定日
     */
    @Alias("納品予定日")
    @ApiModelProperty("納品予定日")
    private LocalDate dlvschedDate;

    /**
     * 納品予定日
     */
    @Alias("生産日")
    @ApiModelProperty("生産日")
    private LocalDate productDate;

    /**
     * 生産日 or 納品予定日 yyyy/MM/dd
     */
    @Alias("生産日/納品予定日")
    @ApiModelProperty("生産日/納品予定日 yyyy/MM/dd")
    private String scheduleDateStr;

    /**
     * 検食
     */
    @Alias("検食")
    @ApiModelProperty("検食")
    @PdfTableProp(title = "検食", width = 5.0f, fontSize = 8f, textAlign = TextAlignment.CENTER)
    private String tasteFlg;

    /**
     * 生産計画数
     */
    @Alias("計")
    @ApiModelProperty("計")
    @PdfTableProp(title = "計", width = 7.0f, fontSize = 8f, textAlign = TextAlignment.CENTER)
    private Integer sumQy;
}
