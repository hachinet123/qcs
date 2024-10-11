package com.tre.centralkitchen.common.enums;

public enum UserType {
    TRIAL_GROUP("1", "トライアルグループ"),
    GUEST("2", "お取引先様");

    private String type;
    private String name;

    UserType(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
