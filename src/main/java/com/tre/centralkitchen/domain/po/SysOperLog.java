package com.tre.centralkitchen.domain.po;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("log.sys_oper_log")
@ExcelIgnoreUnannotated
@ApiModel("オペレーションログビジネスオブジェクト")
public class SysOperLog {

    private static final long serialVersionUID = 1L;

    /**
     * ログid(自動インクリメント)
     */
    @ApiModelProperty(value = "ログid(自動インクリメント)")
    @TableId(value = "oper_id", type = IdType.AUTO)
    private Long operId;

    /**
     * タイトル
     */
    @ApiModelProperty(value = "タイトル")
    private String title;

    /**
     * 業務類型(0その他 1新規 2変更 3削除 4抽出 5ダウンロード 6取込)
     */
    @ApiModelProperty(value = "業務類型(0その他 1新規 2変更 3削除 4抽出 5ダウンロード 6取込)")
    private Integer businessType;

    /**
     * メソッド名称
     */
    @ApiModelProperty(value = "メソッド名称")
    private String method;

    /**
     * 請求方式
     */
    @ApiModelProperty(value = "請求方式")
    private String requestMethod;

    /**
     * 操作区分(0その他 1画面 2ジョブ起動API）
     */
    @ApiModelProperty(value = "操作区分(0その他 1画面 2ジョブ起動API）")
    private Integer operatorType;

    /**
     * 操作者id
     */
    @ApiModelProperty(value = "操作者id")
    private String operUserId;

    /**
     * 請求url
     */
    @ApiModelProperty(value = "請求url")
    private String operUrl;

    /**
     * 操作ip
     */
    @ApiModelProperty(value = "操作ip")
    private String operIp;

    /**
     * 操作アドレス(内部ネット　外部ネット)
     */
    @ApiModelProperty(value = "操作アドレス(内部ネット　外部ネット)")
    private String operLocation;

    /**
     * 請求パラメータ
     */
    @ApiModelProperty(value = "請求パラメータ")
    private String operParam;

    /**
     * 返却結果
     */
    @ApiModelProperty(value = "返却結果")
    private String jsonResult;

    /**
     * 操作状態(0正常　1異常)
     */
    @ApiModelProperty(value = "操作状態(0正常　1異常)")
    private Integer status;

    /**
     * エラーメッセージ
     */
    @ApiModelProperty(value = "エラーメッセージ")
    private String errorMsg;

    /**
     * 記録時間
     */
    @ApiModelProperty(value = "記録時間")
    @TableField("ins_time")
    private Date insTime;
}
