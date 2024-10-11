package com.tre.centralkitchen.common.constant.business;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author 10225441
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FixAmountResultModifyConstants {
    public static final String WEB_PAGE_TITLE = "生産指示_定額指示修正";
    public static final String SWAGGER_TITLE = "定額指示修正";
    public static final String BUSINESS_NAME = "fixAmountResultModify";
    public static final String FMT_ERROR_FILE_NAME = "生産指示_定額指示修正_一括登録フォーマット";
    public static final String ERROR_MSG_NOT_EXIST_INITIAL = "不正入力があります。（{}）;";
    public static final String ERROR_MSG_FILL_EXIST = "{}を入力してください。";
    public static final String ERROR_MSG_FILL_EXIST_0 = "{}に0を入力してください。";
    public static final String EXCEL_COL_ERROR_MSG = "エラーメッセージ";
    public static final String EXCEL_COL_TITLE = "計画あり商品（修正）、計画なし商品（新規レコード追加）を記入できます。";
    public static final String EXCEL_COL_SUB_TITLE = "↓必須";
    public static final String EXCEL_COL_SUB_TITLE_2 = "※原価、売価入力しない場合、取り込みするときに、マスタより自動取得とする";
    public static final String EXCEL_COL_CENTER_ID = "センターコード";
    public static final String EXCEL_COL_CALL_CODE = "品番";
    public static final String EXCEL_COL_ITEM_ID = "JAN";
    public static final String EXCEL_COL_MAIL_NO = "便";
    public static final String EXCEL_COL_STORE_ID = "店舗コード";
    public static final String EXCEL_COL_PLACE_ID = "産地コード";
    public static final String EXCEL_COL_BATCH_GROUP_NO = "ロットNo";
    public static final String EXCEL_COL_QTY = "数量";
    public static final String EXCEL_COL_WEIGHT = "重量";
    public static final String EXCEL_COL_PC_PRICE = "PC原価";
    public static final String EXCEL_COL_ITEM_PRICE = "店原価";
    public static final String EXCEL_COL_PRICE = "店売価";
}
