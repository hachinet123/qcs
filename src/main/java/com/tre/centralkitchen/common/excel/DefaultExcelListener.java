package com.tre.centralkitchen.common.excel;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelAnalysisException;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.tre.centralkitchen.common.utils.JsonUtils;
import com.tre.centralkitchen.common.utils.ValidatorUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Excel import listener
 *
 * @author Yjoioooo
 * @author JDev
 */
@Slf4j
@NoArgsConstructor
public class DefaultExcelListener<T> extends AnalysisEventListener<T> implements ExcelListener<T> {

    /**
     * Whether Validator inspection, the default is yes
     */
    private Boolean isValidate = Boolean.TRUE;

    /**
     * excel header data
     */
    private Map<Integer, String> headMap;

    /**
     * import receipt
     */
    private ExcelResult<T> excelResult;

    public DefaultExcelListener(boolean isValidate) {
        this.excelResult = new DefautExcelResult<>();
        this.isValidate = isValidate;
    }

    /**
     * handle exception
     *
     * @param exception ExcelDataConvertException
     * @param context   Excel context
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        String errMsg = null;
        if (exception instanceof ExcelDataConvertException) {
            // If the conversion of a certain cell is abnormal, the specific row number can be obtained
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
            Integer rowIndex = excelDataConvertException.getRowIndex();
            Integer columnIndex = excelDataConvertException.getColumnIndex();
            errMsg = StrUtil.format("line{}-line{}-header{}: parsing exception<br/>",
                    rowIndex + 1, columnIndex + 1, headMap.get(columnIndex));
            if (log.isDebugEnabled()) {
                log.error(errMsg);
            }
        }
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) exception;
            Set<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations();
            String constraintViolationsMsg = constraintViolations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            errMsg = StrUtil.format("Data verification exception in line{}: {}", context.readRowHolder().getRowIndex() + 1, constraintViolationsMsg);
            if (log.isDebugEnabled()) {
                log.error(errMsg);
            }
        }
        excelResult.getErrorList().add(errMsg);
        throw new ExcelAnalysisException(errMsg);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headMap = headMap;
        log.debug("Parse to a header data: {}", JsonUtils.toJsonString(headMap));
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        if (isValidate) {
            ValidatorUtils.validate(data);
        }
        excelResult.getList().add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.debug("All data analysis completed！");
    }

    @Override
    public ExcelResult<T> getExcelResult() {
        return excelResult;
    }

}
