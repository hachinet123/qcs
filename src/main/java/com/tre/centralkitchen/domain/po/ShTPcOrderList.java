package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("dbo.shT_PcOrderList")
public class ShTPcOrderList {
    @TableField("BranchCD")
    private Integer branchCd;
    @TableField("JAN")
    private String itemId;
    @TableField("Bin")
    private Integer mailNo;
    @TableField("ListNo")
    private Integer listNo;
    @TableField("Level1")
    private Integer level1;
    @TableField("RowNo")
    private Integer rowNo;
    @TableField("StartDate")
    private Date startDate;
    @TableField("EndDate")
    private Date endDate;
    @TableField("UpdateUser")
    private Integer updateUser;
    @TableField("UpdateDate")
    private Date updateDate;
    @TableField("PcCode")
    private Integer pcCode;
}
