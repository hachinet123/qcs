package com.tre.jdevtemplateboot.common.enums;

public enum ExceptionHandlerEnums {
    PARAM_VALIDATE_ERROR(400, 40001, "パラメータが間違いました。"),
    REQUEST_LIMITER_ERROR(400, 40002, "リクエストが重複です、しばらくお待ちください。"),
    INVALID_TOKEN_ERROR(401, 40101, "再度登録してください。"),
    UNAUTHORIZED_ERROR(401, 40102, "権限がありません。"),
    UNAUTHENTICATED_ERROR(401, 40103, "再度登録してください。"),
    INVALID_USER_ERROR(401, 40104, "権限がないので、再度登録してください。"),
    REQUEST_TOKEN_CATEGORY_ERROR(401, 40105, "権限がないので、再度登録してください。"),
    UNAUTHORIZED_FILE_ERROR(401, 40106, "権限がありません。"),
    SERVER_INTERNAL_ERROR(500, 50001, "システム管理者へ連絡してください。"),
    DATA_DUPLICATION_ERROR(500, 50002, "データベースにはすでにこのレコードが存在します、システム管理者へ連絡ください。"),
    CUSTOM_VALIDATION_ERROR(500, 50003, "カスタム検証例外。");

    private int status;
    private int code;
    private String msg;

    private ExceptionHandlerEnums(int status, int code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    public int getStatus() {
        return this.status;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
