package com.tre.centralkitchen.domain.vo.system;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MtSectionVo {

    /**
     * ID
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * プロセスセンターID
     */
    @ApiModelProperty("center_id")
    private Integer centerId;

    /**
     * 係名
     */
    @ApiModelProperty("name")
    private String name;

}
