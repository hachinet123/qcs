package com.tre.centralkitchen.domain.bo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MtSettingBo {
    private Integer userId;
    private Integer centerId;
    private Integer sectionId;
    private Integer languageId;
}
