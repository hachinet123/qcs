package com.tre.centralkitchen.common.enums;

import lombok.Getter;

@Getter
public enum OperatorType {
    OTHER(99),
    MANAGE(1);
    private Integer code;

    OperatorType(Integer code) {
        this.code = code;
    }
}
