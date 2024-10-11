package com.tre.centralkitchen.service;

/**
 * <p>
 * システム自動確定 服务类
 * </p>
 *
 * @author JDev
 * @since 2023-04-18
 */
public interface IMtMailAutoConfirmService {

    boolean hinemosLock(Integer centerId, Integer mailNo, Boolean flag) throws InterruptedException;

    boolean hinemosUnlock(Integer centerId);
}
