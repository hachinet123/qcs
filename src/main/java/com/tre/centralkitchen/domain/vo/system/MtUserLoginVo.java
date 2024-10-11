package com.tre.centralkitchen.domain.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class MtUserLoginVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 社員番号
     */
    @ApiModelProperty("社員番号")
    private String code;

    /**
     * 備考
     */
    @ApiModelProperty("備考")
    private String memo;

    /**
     * 削除フラグ
     */
    @ApiModelProperty("削除フラグ")
    private Integer fDel;


}
