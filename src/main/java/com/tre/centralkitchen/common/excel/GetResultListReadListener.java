package com.tre.centralkitchen.common.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.utils.ReflectUtils;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 10225441
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetResultListReadListener<T> implements ReadListener<T> {
    private List<T> list;
    private Class<T> clazz;
    private int rowNum;

    public GetResultListReadListener(List<T> list) {
        this.list = Objects.requireNonNullElseGet(list, ArrayList::new);
        this.clazz = null;
        this.rowNum = 0;
    }

    public GetResultListReadListener(List<T> list, Class<T> clazz) {
        this.list = Objects.requireNonNullElseGet(list, ArrayList::new);
        this.clazz = clazz;
        this.rowNum = 0;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        ReadListener.super.onException(exception, context);
    }

    private static final String ERROR_MSG = "ファイルコンテンツのフォーマットが間違っています。再インポートしてください";

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        if (clazz == null) {
            ReadListener.super.invokeHead(headMap, context);
        } else {
            List<Object> aList = ReflectUtils.getAnnotationValueList(clazz, ExcelProperty.class, "value");
            if (aList.isEmpty() || headMap.isEmpty()) {
                throw new SysBusinessException();
            }
            List<String> headerList = aList.stream().map(o -> Array.get(o, rowNum).toString()).collect(Collectors.toList());
            Set<String> tmpSet = new HashSet<>(headerList);
            Map<Integer, String> tmpMap = new HashMap<>();
            headMap.forEach((key, val) -> {
                try {
                    tmpMap.put(key, val.getStringValue() != null ? val.getStringValue() : StringConstants.BLANK);
                } catch (Exception e) {
                    tmpMap.put(key, StringConstants.BLANK);
                }
            });
            checkExcelHeaderWithAnnotation(headerList, tmpSet, tmpMap);
            rowNum++;
        }
    }

    private void checkExcelHeaderWithAnnotation(List<String> headerList, Set<String> tmpSet, Map<Integer, String> tmpMap) {
        if (tmpSet.size() == 1) {
            if (tmpMap.get(0).equals(headerList.get(0))) {
                Map<Integer, String> map = new HashMap<>(tmpMap);
                map.remove(0);
                map.forEach((key, val) -> {
                    if (val != null && !val.isBlank()) {
                        throw new SysBusinessException(ERROR_MSG);
                    }
                });
            } else {
                throw new SysBusinessException(ERROR_MSG);
            }
        } else {
            tmpMap.forEach((key, val) -> {
                if (!Objects.equals(headerList.get(key), val)) {
                    throw new SysBusinessException(ERROR_MSG);
                }
            });
        }
    }

    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        log.info(JSON.toJSONString(t));
        list.add(t);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        ReadListener.super.extra(extra, context);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info(Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return ReadListener.super.hasNext(context);
    }
}
