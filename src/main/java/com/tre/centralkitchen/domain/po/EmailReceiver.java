package com.tre.centralkitchen.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tre.centralkitchen.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("")
public class EmailReceiver extends BaseEntity {

    private Integer taskId;

    private Integer subTaskId;

    private String taskName;

    private String toEmail;

    private String ccEmail;

    private String bccEmail;








}
