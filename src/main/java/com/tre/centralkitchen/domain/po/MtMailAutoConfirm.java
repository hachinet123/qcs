package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * システム自動確定
 * </p>
 *
 * @author JDev
 * @since 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("centralkitchen.mt_mail_auto_confirm")
public class MtMailAutoConfirm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * センターid
     */
    private Integer centerId;

    /**
     * 便
     */
    private Integer mailNo;


}
