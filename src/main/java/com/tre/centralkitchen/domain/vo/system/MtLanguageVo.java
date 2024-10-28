package com.tre.centralkitchen.domain.vo.system;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MtLanguageVo {

    /**
     * 言語ID
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 言語名
     */
    @ApiModelProperty("name")
    private String name;

    /**
     * 得意先名
     */
    @ApiModelProperty("言語名 (英語)")
    private String name_e;

}
