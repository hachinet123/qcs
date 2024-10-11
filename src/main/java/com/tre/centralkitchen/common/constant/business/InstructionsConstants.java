package com.tre.centralkitchen.common.constant.business;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author 10225441
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InstructionsConstants {
    public static final String WEB_PAGE_TITLE_1 = "生産指示_生産指示書";
    public static final String WEB_PAGE_FILE_1 = "productionInstructions";
    public static final String PDF_PAGE_TITLE_1 = "■■  生産指示書  ■■";
    public static final String WEB_PAGE_TITLE_2 = "生産指示_振分指示書";
    public static final String WEB_PAGE_FILE_2 = "distributionInstructions";
    public static final String PDF_PAGE_TITLE_2 = "■■  振分指示書  ■■";
    public static final String WEB_PAGE_TITLE_3 = "生産指示_ラベルチェックリスト";
    public static final String WEB_PAGE_FILE_3 = "labelCheckInstructions";
    public static final String WEB_PAGE_TITLE_4 = "作業報告書";
    public static final String WEB_PAGE_FILE_4 = "workReport";
    public static final String CSV_HEADER_1 = "センター,納品予定日,便,ライン,作業グループ,品番,商品名,生産計画,計量,規格,内容量,産地,ロットGP";
    public static final String CSV_HEADER_2 = "センター,納品予定日,便,ライン,作業グループ,品番,商品名,規格,店舗コード,店舗名,数量";
    public static final String ATTR_MAIL_NO = "mailNo";
    public static final String ATTR_DATE_TYPE = "dateType";
    public static final String PRINT_DATE_TIME = "印刷日時: ";
    public static final String PRODUCT_DATE_TIME = "生産日: ";
    public static final String SCHEDULE_DATE_TIME = "納品予定日: ";
    public static final String LINE_WITH_COLON = "ライン: ";
    public static final String TOTAL_WITH_COLON = "合計: ";
    public static final String WORK_GROUP_WITH_COLON = "作業グループ: ";
    public static final String CALL_CODE_WITH_COLON = "品番：";
    public static final String ITEM_NAME_WITH_COLON = "商品名：";
    public static final String ITEM_SPEC_WITH_COLON = "規格：";
    public static final String CLOSED_STORE = "閉店";
    public static final String MAX_STORE_NUM = "最大(180店舗)";
    public static final String FIELD_WORK_GROUP_ID = "作業グループ";
    public static final String FIELD_MAIL_NO = "便";
    public static final String FIELD_TOTAL = "合計";
}
