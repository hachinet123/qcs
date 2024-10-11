package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class AppendedSearchBo {

    @ApiModelProperty(value = "センター", required = true)
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;

    @ApiModelProperty(value = "便", required = true)
    @NotNull(message = SysConstantInfo.MAIL_NO_NOT_EMPTY)
    private String mailNo;

    private List<String> mailNoList;

    @ApiModelProperty(value = "ライン", required = true)
    @NotNull(message = SysConstantInfo.LINE_NOT_EMPTY)
    private String lineId;

    private List<String> lineIdList;

    @ApiModelProperty(value = "JAN")
    private String itemId;

    @ApiModelProperty(value = "呼出品番")
    private Integer callCode;

    @ApiModelProperty(value = "商品名")
    private String itemName;

    @ApiModelProperty(value = "開始日", required = true)
    @NotNull(message = SysConstantInfo.STR_DATE_NOT_EMPTY)
    private Date startDate;

    @ApiModelProperty(value = "終了日", required = true)
    @NotNull(message = SysConstantInfo.END_DATE_NOT_EMPTY)
    private Date endDate;

    @ApiModelProperty(value = "伝票番号")
    private Integer slipCode;

    @ApiModelProperty(value = "行番号")
    private Integer lineNo;

    @ApiModelProperty(value = "返品伝票")
    private Boolean mailNo20;

    public void build() {
        if (this.lineId != null) {
            setLineIdList(Arrays.asList(this.lineId.split(",")));
        }
        if (this.mailNo20 != null && this.mailNo20) {
            this.mailNo = this.mailNo + ',' + 20;
        }
        if (this.mailNo != null) {
            setMailNoList(Arrays.asList(this.mailNo.split(",")));
        }
    }
}
