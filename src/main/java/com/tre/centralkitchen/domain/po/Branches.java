package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author JDev
 * @since 2023-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.branches")
public class Branches {

    private static final long serialVersionUID = 1L;

    /**
     * 拠点コード
     */
    private Integer branchcd;

    /**
     * 拠点名
     */
    private String branchname;

    /**
     * 拠点名称カナ
     */
    private String branchnameRead;

    /**
     * 拠点略称
     */
    private String branchnameAbbr;

    private Integer blockcd;

    private Integer footholdcd;

    private Integer areacd;

    private Integer companycd;

    private Integer countrycd;

    private Integer statecd;

    private String postalcd;

    private String municipality;

    private String municipalityRead;

    private String streetaddress;

    private String streetaddressRead;

    private String tel;

    private String fax;

    private String memo;

    private Integer branchformatcd;

    private Integer branchgroupcd;

    private Integer branchscalecd;

    private Integer branchtype;

    private Integer branchlinecd;

    private Integer customattrgroupcd;

    private Integer dctype;

    private Integer fctype;

    private Integer isclosed;

    private String openeddate;

    private String closeddate;

    private Integer postype;

    /**
     * 登録者ID
     */
    private String author;

    /**
     * 更新者ID
     */
    private String maintainer;

    /**
     * 登録日時
     */
    private Date registered;

    /**
     * 更新日時
     */
    private Date modified;


}
