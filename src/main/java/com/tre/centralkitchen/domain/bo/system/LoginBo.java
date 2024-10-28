package com.tre.centralkitchen.domain.bo.system;


import com.tre.centralkitchen.common.domain.BaseEntityBo;
import lombok.Data;

@Data
public class LoginBo extends BaseEntityBo {
    private String code;
}
