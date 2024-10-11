package com.tre.centralkitchen.common.constant.business;

public class MessageConstants {

    public static final String SEND_MESSAGE =
            "菌検査 担当者様\n" +
            "以下の検査の依頼がきています。\n" +
            "管理番号：%s\n" +
            "タイトル：%s\n" +
            "検体数：%s\n" +
            "\t\t\t\t\t\t\t\t\t\n" +
            "セントラルキッチンシステムはこちら。\n" +
            "http://10.2.3.112:10803/#/login\n" +
            "\t\t\t\t\n" +
            "※セントラルキッチンシステムからの自動送信メールです。\n" +
            "※このメールへの返信はお受けしておりません。";


    public static final String EMAIL_SUBJECT = "【菌検査依頼】";


    public static final String SEND_MESSAGE_BACTERIA_CHECK_RESULT = "菌検査依頼者様\t\n" +
            "\t\n" +
            "以下の検査の結果となりました。\t\n" +
            "管理番号：%s\t\n" +
            "タイトル：%s\t\n" +
            "判定：%s\t\n" +
            "\t\t\n" +
            "セントラルキッチンシステムはこちら。\t\n" +
            "http://10.2.3.112:10803/#/login\t\n" +
            "\t\n" +
            "※セントラルキッチンシステムからの自動送信メールです。\t\n" +
            "※このメールへの返信はお受けしておりません。\n";

    public static final String EMAIL_SUBJECT_BACTERIA_CHECK_RESULT = "【菌検査結果報告】";
}
