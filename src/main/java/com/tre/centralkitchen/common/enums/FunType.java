package com.tre.centralkitchen.common.enums;

public enum FunType {
    FOREIGN(0L, "デフォルト"),
    REGISTERED(1L, "PC受注"),
    MASTER(2L, "マスターメンテ"),
    USER(3L, "ユーザーメンテ"),
    BACTERIACHECK(4L,"菌検査"),
    WAREHOUSEITEM(5L,"在庫");


    private Long code;
    private String name;

    FunType(Long code, String name) {
        this.code = code;
        this.name = name;
    }

    public Long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
