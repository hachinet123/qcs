package com.tre.centralkitchen.domain.vo.system;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MtSettingVo {
    /**
     * ユーザーID
     */
    @ApiModelProperty("user_id")
    private Integer userId;

    /**
     * センター
     */
    @ApiModelProperty("user_id")
    private Integer center_id;

    /**
     * セクションID
     */
    @ApiModelProperty("section_id")
    private Integer sectionId;

    /**
     * 言語ID
     */
    @ApiModelProperty("language_id")
    private Integer languageId;
}
