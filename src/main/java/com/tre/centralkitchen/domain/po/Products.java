package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * トライアル商品マスタ
 * </p>
 *
 * @author JDev
 * @since 2023-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.products")
public class Products implements Serializable {

    private static final long serialVersionUID = 1L;

    private String productcd;

    private String itf;

    @TableField("productname")
    private String productName;
    @TableField("productname_read")
    private String productnameRead;
    @TableField("productname_abbr")
    private String productnameAbbr;

    private String posprodname;
    @TableField("posprodname_read")
    private String posprodnameRead;

    private String specname;
    @TableField("specname_read")
    private String specnameRead;

    private Integer makercd;

    private String makerprodcd;

    private Integer departmentcd;

    private Integer subdeptcd;

    private Integer varietycd;

    private Integer itemcd;

    private String fixedprice;

    private Integer taxtype;

    private String brandname;

    private Integer colorcd;

    private Integer sizecd;

    private Integer isbundled;

    private Integer tempzonecd;

    private Integer productcontroltype;

    private Integer brandtype;

    private Integer developtype;

    private Integer seasontype;

    private Integer producthandletype;

    private Integer materialtype;

    private Integer lifetime;

    private Integer lifeterm;

    private Integer volume;

    private Integer volumeunit;

    private Integer height;

    private Integer width;

    private Integer depth;

    private Integer innercaseheight;

    private Integer innercasewidth;

    private Integer innercasedepth;

    private Integer innercaseqty;

    private Integer innercaseweight;

    private Integer outercaseheight;

    private Integer outercasewidth;

    private Integer outercasedepth;

    private Integer outercaseqty;

    private Integer outercaseweight;

    private String memo;

    private Integer activestatus;

    private String productitemcd;

    private String costprice;

    private Integer suppliercd;

    private String salesprice;

    private Integer orderunit;

    private Integer orderunitqty;

    private Integer displayunit;

    private BigDecimal firstbuyercd;

    private String explain;

    private Integer year;

    private String popname;

    private Integer tastperiod;

    private String uselesssku;

    private Integer pospastflg;

    private String packproductcd;

    private String caseproductcd;

    private LocalDateTime endofsalesdate;

    private Integer manufacturercd;

    private LocalDateTime salestartdate;

    private Integer innerpalletqty;

    private Integer subsegmentcd;

    private String author;

    private String maintainer;

    private LocalDateTime registered;

    private LocalDateTime modified;


}
