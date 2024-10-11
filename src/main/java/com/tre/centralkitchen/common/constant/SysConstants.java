package com.tre.centralkitchen.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author 10225441
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SysConstants {
    public static final String SERIAL_VERSION_UID = "serialVersionUID";
    public static final String PDF_FONT_PATH = "static/font/meiryo.ttc";
    public static final String PDF_FONT_PATH_BK = "static/font/msgothic.ttc";
    public static final String FMT_PATH_PREFIX = "static/fmt/";
    public static final String FMT_CONFIG_YAML = "common-fmt-download.yml";
    public static final String GBK = "GBK";
    public static final String FILE_DELETE_MSG = "Delete file - {} - {}";
    public static final String SCHEDULED_MSG_CLEAN_FOLDER_ST = "Scheduled tasks - Clean folder {} - start";
    public static final String SCHEDULED_MSG_CLEAN_FOLDER_ED = "Scheduled tasks - Clean folder {} - end";
    public static final String MSG_SUCCESS = "success";
    public static final String MSG_FAILURE = "failure";
    public static final String TYPE_OPERATING_SYSTEM_WINDOWS = "windows";
    public static final String PATH_DRIVER_D = "D:";
    public static final String PROP_OS_NAME = "os.name";
    public static final String APPENDED_FILE_NAME = "仕入出庫_出庫データ追加_一括登録フォーマット";
    public static final String APPENDED_UPDATE_FILE_NAME = "仕入出庫_出庫データ追加_既存修正";
    public static final String APPENDED_FILE_NAME_CSV = "仕入出庫_出庫データ追加修正";
    public static final String ISSUE_CHECK_DATA = "出庫データ確認";
    public static final String ITEM_CENTER_MATERIAL_FILE_NAME = "在庫_原材料安全在庫管理_一括登録フォーマットファイル";
    public static final String EXAMINATION_FILE_NAME_CSV = "検食不要一覧";
    public static final String WORK_REPORT_FILE_NAME_CSV = "生産_作業報告書";
    public static final String DAILY_INVENTORY_FILE_NAME = "在庫_日次棚卸_一括登録フォーマットファイル";
    public static final String DOWNLOAD_LINK_FILE_NAME = "各種ダウンロード_ラベル発行実績";
    public static final String SHOHIN_FILE_NAME_CSV = "商品";
    public static final int PDF_NUMBER = 10;
}
