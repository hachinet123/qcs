package com.tre.centralkitchen.common.constant.business;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailConstants {
    //便の個別設定
    public static final String MT_MAIL_ITEM_CSV_NAME = "マスタ_便の個別設定";
    public static final String MT_MAIL_ITEM_CSV_HEADER = "JAN,品番,商品名,店舗コード,店舗名,便,備考,操作";
    public static final String FIELD_FLAG = "flag";
    public static final String FIELD_FILE_NAME = "fileName";
    public static final String BUSINESS_NAME = "mtMailItem";
    public static final String EXCEL_COL_CENTER_ID = "センターコード";
    public static final String EXCEL_COL_ITEM_ID = "JAN";
    public static final String EXCEL_COL_MAIL_NO = "便";
    public static final String EXCEL_COL_STORE_ID = "店舗コード";
    public static final String ERROR_MSG_LENGTH_ERROR = "{}[{}]最大50桁;";
    public static final String ERROR_MSG_NOT_EXIST = "{}入力した[{}]が不正です;";
    public static final String ERROR_MSG_NOT_NULL = "必須項目を入力してください;";
    public static final String ERROR_MSG_NOT_MATCH_01 = "{}[{}]のみ取込してください;";
    public static final String ERROR_MSG_NOT_MATCH_02 = "{}画面で入力した[{}]と同じセンターコードを入力してください;";
    public static final String ERROR_MSG_NOT_MATCH_03 = "{}この[{}]は、このセンターに存在しません;";
    //便商品別確認
    public static final String LIST_MAIL_ITEM_CSV_NAME = "マスタ_便商品別確認";
    public static final String LIST_MAIL_ITEM_CSV_HEADER = "センター,ライン,JAN,品番,商品名,店舗コード,便,設定区分";
    //便一覧
    public static final String MAIL_GENERAL_VIEW_CSV_NAME = "マスタ_便一覧";
    public static final String MAIL_GENERAL_VIEW_CSV_HEADER = "センター,便,リードタイム,店舗数,説明,備考,制約";
    //生産実績取得
    public static final String GET_ACTUAL_PRODUCTION_CSV_NAME = "生産実績取得_CSV";
    public static final String ACTUAL_PRODUCTION_CONFIRM_CSV_NAME = "生産実績確定_CSV";
    public static final String ACTUAL_PRODUCTION_RECOVERY_CSV_NAME = "生産実績確定復旧_CSV";
    public static final String FIX_AMOUNT_RESULT_CONFIRM_CSV_NAME = "定額実績確定_CSV";
    public static final String TR_BACTERIA_CHECK_CSV_NAME = "菌検査_菌検査一覧_";
    public static final String MT_WAREHOUSE_ITEM_CSV_NAME = "マスタ_原材料倉庫割当";
    public static final String MT_CENTERDLV_STORE_CSV_NAME = "マスタ_センター別ライン別管轄店舗";
    public static final String CHECK_ITEM_STORE = "JANと地域";
    public static final String MT_ITEM_CENTER_MATERIAL_CSV_NAME = "マスタ_原材料別安全在庫管理";
    public static final String WAREHOUSE_ITEM_PREDICTION_CSV_NAME = "在庫_在庫予測_";

    public static final String CONFIRMED = "確定済";
    public static final String NOT_CONFIRMED = "未確定";
}
