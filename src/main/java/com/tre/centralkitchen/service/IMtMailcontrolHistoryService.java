package com.tre.centralkitchen.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tre.centralkitchen.domain.po.MtMailcontrolHistory;

/**
 * <p>
 * 便コントロール履歴 服务类
 * </p>
 *
 * @author JDev
 * @since 2023-02-28
 */
public interface IMtMailcontrolHistoryService extends IService<MtMailcontrolHistory> {

    void backup(Integer centerId, Integer mailNo);
}
