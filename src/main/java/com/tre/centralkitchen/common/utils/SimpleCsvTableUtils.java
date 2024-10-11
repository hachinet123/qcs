package com.tre.centralkitchen.common.utils;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Simple Csv Export Util</p>
 *
 * @author 10225441
 * @see CsvUtil#getWriter(Writer)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SimpleCsvTableUtils {
    /**
     * content type (default csv).
     */
    private static final String CONTENT_TYPE = "text/csv";
    /**
     * character encoding (default utf-8).
     */
    private static final String CHARACTER_ENCODING = "UTF-8";
    /**
     * csv file encoding (default shift_jis).
     */
    private static final String CSV_FILE_ENCODING = "Shift_JIS";
    /**
     * content disposition key.
     */
    private static final String CONTENT_DISPOSITION = "Content-disposition";
    /**
     * content disposition value prefix.
     */
    private static final String CONTENT_DISPOSITION_PREFIX = "attachment;filename=";
    /**
     * content disposition value suffix.
     */
    private static final String CONTENT_DISPOSITION_SUFFIX = ".csv";
    /**
     * error message.
     */
    private static final String RSP_ERROR_MESSAGE = "HttpServletResponse is null!";
    /**
     * default date format
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";

    /**
     * <h1>One step method of export csv file to response stream.</h1>
     * <p>Convert java bean list to csv file stream and print csv file stream to http servlet response output stream.</p>
     * <p>Use bean's alias as column headers.</p>
     * <p></p>
     * <p>Notice:</p>
     * <p>The order of the fields in the entity class must be consistent with the order of the column headers in the csv file!</p>
     * <p></p>
     * <p>Example:</p>
     * <p>{@code SimpleCsvTableUtils.easyPrintCsvToRespStream(response, "xxx_xxx", businessPoList, BusinessPo.class);}</p>
     * <p>File name: xxx_xxx_202212051532.csv</p>
     *
     * @param response Http servlet response
     * @param fileName Csv file's name
     * @param beanList Csv data's list
     * @param clazz    Bean's class of data list
     * @see Alias#value()
     */
    public static void easyCsvExport(HttpServletResponse response, String fileName, List<?> beanList, Class<?> clazz) {
        if (response == null) {
            throw new SysBusinessException(RSP_ERROR_MESSAGE);
        }
        String tmpFileName = getFileName(CharSequenceUtil.isBlank(fileName) ? "" : fileName, DEFAULT_DATE_FORMAT);
        printBeansToRespStream(response, tmpFileName, beanList, clazz, new String[]{});
    }

    /**
     * <p>Convert java bean list to csv file stream and print csv file stream to http servlet response output stream.</p>
     * <p>Use bean's alias as column headers or use string array of headers as column headers.</p>
     *
     * @param response Http servlet response
     * @param fileName Csv file's name
     * @param beanList Data list
     * @param clazz    Bean's class
     * @param headers  Csv file's column headers
     * @see Alias#value()
     */
    public static void printBeansToRespStream(HttpServletResponse response, String fileName, List<?> beanList, Class<?> clazz, String[] headers) {
        if (response == null) {
            throw new SysBusinessException(RSP_ERROR_MESSAGE);
        }
        try (OutputStream out = response.getOutputStream()) {
            setResponseHeader(response, fileName);
            CsvWriter writer = CsvUtil.getWriter(new PrintWriter(out, false, Charset.forName(CSV_FILE_ENCODING)));
            List<Object> aliasList = ReflectUtils.getAnnotationValueList(clazz, Alias.class, "value");
            String[] columnNames = aliasList.stream().map(Object::toString).toArray(String[]::new);
            if (CollUtil.isNotEmpty(beanList)) {
                boolean isFirst = true;
                Map<String, Object> map;
                for (Object bean : beanList) {
                    map = BeanUtil.beanToMap(bean, columnNames);
                    if (isFirst) {
                        renderHeaderLine(headers, writer, map);
                        isFirst = false;
                    }
                    writer.writeLine(Convert.toStrArray(map.values()));
                }
            } else {
                Object o = clazz.getDeclaredConstructor().newInstance();
                Map<String, Object> map = BeanUtil.beanToMap(o, columnNames);
                renderHeaderLine(headers, writer, map);
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    private static void renderHeaderLine(String[] headers, CsvWriter writer, Map<String, Object> map) {
        if (headers.length > 0) {
            writer.writeHeaderLine(headers);
        } else {
            writer.writeHeaderLine(map.keySet().toArray(new String[0]));
        }
    }

    /**
     * <p>Convert strings to csv file stream and print csv file stream to http servlet response output stream.</p>
     * <p>Use the first string of string list as column headers.</p>
     *
     * @param response Http servlet response
     * @param fileName Csv file's name
     * @param strList  Data's list
     */
    public static void printStringListToRespStream(HttpServletResponse response, String fileName, List<String> strList) {
        if (response == null) {
            throw new SysBusinessException(RSP_ERROR_MESSAGE);
        }
        try (OutputStream out = response.getOutputStream()) {
            setResponseHeader(response, fileName);
            CsvWriter writer = CsvUtil.getWriter(new PrintWriter(out, false, Charset.forName(CSV_FILE_ENCODING)));
            writer.write(strList);
            writer.close();
        } catch (IOException e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    public static void csvExport(HttpServletResponse response, String fileName, List<LinkedHashMap<String, Object>> dataList, String[] headers) {
        if (response == null) {
            throw new SysBusinessException(RSP_ERROR_MESSAGE);
        }
        String tmpFileName = getFileName(CharSequenceUtil.isBlank(fileName) ? "" : fileName, DEFAULT_DATE_FORMAT);
        try (OutputStream out = response.getOutputStream()) {
            setResponseHeader(response, tmpFileName);
            CsvWriter writer = CsvUtil.getWriter(new PrintWriter(out, false, Charset.forName(CSV_FILE_ENCODING)));

            writer.writeHeaderLine(headers);

            for (Map<String, Object> map : dataList) {
                writer.writeLine(Convert.toStrArray(map.values()));
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    /**
     * <p>Convert strings to csv file stream and print csv file stream to http servlet response output stream.</p>
     * <p>Use the map's key as column headers.</p>
     *
     * @param response Http servlet response
     * @param fileName Csv file's name
     * @param mapList  Data's maps
     */
    public static void printMapListToRespStream(HttpServletResponse response, String fileName, List<Map<String, Object>> mapList) {
        if (response == null) {
            throw new SysBusinessException(RSP_ERROR_MESSAGE);
        }
        try (OutputStream out = response.getOutputStream()) {
            setResponseHeader(response, fileName);
            CsvWriter writer = CsvUtil.getWriter(new PrintWriter(out, false, Charset.forName(CSV_FILE_ENCODING)));
            if (CollUtil.isNotEmpty(mapList)) {
                boolean isFirst = true;
                for (Map<String, Object> map : mapList) {
                    if (isFirst) {
                        writer.writeHeaderLine(map.keySet().toArray(new String[0]));
                        isFirst = false;
                    }
                    writer.writeLine(Convert.toStrArray(map.values()));
                }
                writer.flush();
            }
            writer.close();
        } catch (IOException e) {
            throw new SysBusinessException(e.getMessage());
        }
    }

    /**
     * <p>Get Column headers.</p>
     * <p>This string is separated by commas.</p>
     *
     * @param headerStr Column headers(separated by commas)
     * @return Column headers array.
     */
    public static String[] getHeaders(String headerStr, String separator) {
        return headerStr.split(separator);
    }

    /**
     * <p>Get csv file's name.</p>
     *
     * @param fileName Csv file's name
     * @return Encode csv file's name
     */
    public static String getFileName(String fileName) {
        return URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("\\+", "%20");
    }

    /**
     * <p>Get csv file's name with timestamp.</p>
     *
     * @param fileName    Csv file's name.
     * @param datePattern Date's format(example: yyyyMMdd)
     * @return Encode csv file's name
     */
    public static String getFileName(String fileName, String datePattern) {
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern));
        return getFileName(fileName + "_" + currentDate);
    }

    /**
     * <p>Set http response headers.</p>
     *
     * @param response Http servlet response
     * @param fileName Csv file's name
     */
    private static void setResponseHeader(HttpServletResponse response, String fileName) {
        if (response == null) {
            throw new SysBusinessException(RSP_ERROR_MESSAGE);
        }
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setHeader(CONTENT_DISPOSITION, CONTENT_DISPOSITION_PREFIX + fileName + CONTENT_DISPOSITION_SUFFIX);
    }
}
