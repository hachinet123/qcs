package com.tre.centralkitchen.common.enums;

import lombok.Getter;

@Getter
public enum ItemType {
    product("1"),
    kit("2");

    private String code;
    ItemType(String code) {
        this.code = code;
    }
}
