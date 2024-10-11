package com.tre.centralkitchen.common.excel;

import java.util.List;

/**
 * excel return object
 *
 * @author JDev
 */
public interface ExcelResult<T> {

    /**
     * object list
     */
    List<T> getList();

    /**
     * error list
     */
    List<String> getErrorList();

    /**
     * import receipt
     */
    String getAnalysis();
}
