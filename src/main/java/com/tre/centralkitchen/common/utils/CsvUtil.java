package com.tre.centralkitchen.common.utils;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvWriter;
import com.tre.centralkitchen.common.annotation.CsvColumn;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @date 2022-11-30
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsvUtil {

    public static <T> void downloadFromList(String fileName, List<T> list, HttpServletResponse response, Class<T> clazz, boolean includeHeader) throws IOException, IllegalAccessException {
        FileUtils.setAttachmentResponseHeader(response, fileName);
        response.setContentType("application/octet-stream");
        List<CsvColumnInfo> columnList = getColumnInfos(clazz);
        OutputStream os = response.getOutputStream();
        CsvWriter writer = new CsvWriter(new PrintWriter(os));
        CsvData csvData = getCsvData(list, columnList, includeHeader);
        writer.write(csvData);
        writer.flush();
        writer.close();
    }

    private static <T> List<CsvColumnInfo> getColumnInfos(Class<T> clazz) {
        List<CsvColumnInfo> columnList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        //search csvColumn
        for (Field field : fields) {
            if (field.isAnnotationPresent(CsvColumn.class)) {
                CsvColumn column = field.getAnnotation(CsvColumn.class);
                CsvColumnInfo csvColumnInfo = new CsvColumnInfo(column.position(), column.columnName(), field);
                columnList.add(csvColumnInfo);
            }
        }
        Collections.sort(columnList, Comparator.comparingInt(CsvColumnInfo::getPostion));
        return columnList;
    }

    private static <T> CsvData getCsvData(List<T> list, List<CsvColumnInfo> columnList, boolean includeHeader) throws IllegalAccessException {
        List<String> header = new ArrayList<>();
        Map<String, Integer> headerMap = new HashMap<>();
        if (includeHeader) {
            for (int i = 0; i < columnList.size(); i++) {
                header.add(columnList.get(i).getColumnName());
                headerMap.put(columnList.get(i).getColumnName(), i);
            }
        }

        List<CsvRow> csvRowList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            T object = list.get(i);
            List<String> fieldList = new ArrayList<>();
            for (int j = 0; j < columnList.size(); j++) {
                columnList.get(j).getFiled().setAccessible(true);
                fieldList.add(columnList.get(j).getFiled().get(object).toString());
            }
            CsvRow csvRow = new CsvRow(i, headerMap, fieldList);
            csvRowList.add(csvRow);
        }

        return new CsvData(header, csvRowList);
    }

    @Data
    private static class CsvColumnInfo {
        private int postion;
        private String columnName;
        private Field filed;

        public CsvColumnInfo(int postion, String columnName, Field field) {
            this.postion = postion;
            this.columnName = columnName;
            this.filed = field;
        }
    }


}
