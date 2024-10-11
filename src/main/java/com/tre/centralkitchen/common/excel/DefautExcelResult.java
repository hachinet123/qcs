package com.tre.centralkitchen.common.excel;

import cn.hutool.core.util.StrUtil;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Default excel return object
 *
 * @author Yjoioooo
 * @author JDev
 */
public class DefautExcelResult<T> implements ExcelResult<T> {

    /**
     * data object list
     */
    @Setter
    private List<T> list;

    /**
     * error message list
     */
    @Setter
    private List<String> errorList;

    public DefautExcelResult() {
        this.list = new ArrayList<>();
        this.errorList = new ArrayList<>();
    }

    public DefautExcelResult(List<T> list, List<String> errorList) {
        this.list = list;
        this.errorList = errorList;
    }

    public DefautExcelResult(ExcelResult<T> excelResult) {
        this.list = excelResult.getList();
        this.errorList = excelResult.getErrorList();
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public List<String> getErrorList() {
        return errorList;
    }

    /**
     * Get import receipt
     *
     * @return import receipt
     */
    @Override
    public String getAnalysis() {
        int successCount = list.size();
        int errorCount = errorList.size();
        if (successCount == 0) {
            return "Read failed, no data was parsed";
        } else {
            if (errorCount == 0) {
                return StrUtil.format("Congratulations, all read successfully!Total {}", successCount);
            } else {
                return "";
            }
        }
    }
}
