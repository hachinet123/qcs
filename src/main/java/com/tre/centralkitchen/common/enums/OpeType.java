package com.tre.centralkitchen.common.enums;

public enum OpeType {
    FOREIGN(0L, "デフォルト"),
    INJECTION_STATUS(1L, "受注状況"),
    ACHIEVEMENTS_PRODUCTION(2L, "生産実績取得"),
    DETERMINATION_OBJECTIVES(3L, "生産実績確定"),
    DETERMINATION_OBJECTIVE(4L, "定額実績確定"),
    CORRECTION_INDICATION(5L, "定額指示修正"),
    PRODUCTION_INSTRUCTIONS(6L, "生産指示書"),
    VIBRATION_INSTRUCTION(7L, "振分指示書"),
    PRODUCTION_OBJECTIVES(8L, "生産実績確定復旧"),
    EXTENSION_PRODUCTION(9L, "自動確定保留"),
    ADDITIONAL_DATA(10L, "出庫データ追加修正"),
    CHECK_DATA(11L, "出庫データ確認"),
    WORKING_GROUP(12L, "作業グループ"),
    MAIL(13L, "便"),
    USER(14L, "ユーザー"),
    BACTERIA_CHECK_LIST(15L, "菌検査一覧"),
    BACTERIACHECK_REQUEST(16L, "菌検査依頼"),
    WAREHOUSEITEM_REQUEST(17L, "原材料倉庫割当"),
    CENTERD_LVSTORE_REQUEST(18L, "センター別ライン別管轄店舗"),
    ITEM_CENTER_MATERIAL_REQUEST(19L, "原材料別安全在庫管理"),
    EXAMINATION(20L, "検食不要一覧"),
    DAILY_INVENTORY(21L, "日次棚卸");
    private Long code;
    private String name;

    OpeType(Long code, String name) {
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
