package com.tre.centralkitchen.common.excel;

import com.alibaba.excel.read.listener.ReadListener;

/**
 * Excel import listener
 *
 * @author JDev
 */
public interface ExcelListener<T> extends ReadListener<T> {

    ExcelResult<T> getExcelResult();

}
