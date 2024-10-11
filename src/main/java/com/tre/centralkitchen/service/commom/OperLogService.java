package com.tre.centralkitchen.service.commom;

import com.tre.centralkitchen.common.domain.OperLog;
import org.springframework.scheduling.annotation.Async;

/**
 * 通用 操作日志
 */
public interface OperLogService {

    @Async
    void recordOper(OperLog dto);
}
