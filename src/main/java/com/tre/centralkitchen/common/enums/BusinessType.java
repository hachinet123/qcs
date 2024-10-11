package com.tre.centralkitchen.common.enums;

import lombok.Getter;

@Getter
public enum BusinessType {
    OTHER(99),
    INSERT(1),
    UPDATE(2),
    DELETE(3),
    GRANT(4),
    EXPORT(5),
    IMPORT(6);

    private Integer code;

    BusinessType(Integer code) {
        this.code = code;
    }
}
