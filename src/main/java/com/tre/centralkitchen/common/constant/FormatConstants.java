package com.tre.centralkitchen.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author 10225441
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormatConstants {
    public static final String DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR = "yyyyMMddHHmmss";
    public static final String DATE_TIME_FORMAT_14_WITH_02_SEPARATOR = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_12_WITH_02_SEPARATOR = "yyyy/MM/dd HH:mm";
    public static final String DATE_FORMAT_08_WITH_01_SEPARATOR_01 = "yyyy/MM/dd";
    public static final String DATE_FORMAT = "yyyy年MM月dd日";
    public static final String MONTH_DAY_FORMAT = "MM月dd日";
    public static final String YEAR_MONTH_DAY_HOUR_FORMAT = "yy.MM.dd HH";
    public static final String THOUSANDTH_WITH_SEPARATOR_NO_DECIMAL = ",###";
    public static final String REGEX_PATTERN_MONEY = "^(\\d+|\\d{1,3}(,\\d{3})*)(.\\d{1,2})?$";
    public static final String REGEX_PATTERN_WEIGHT = "^\\d{1,8}(\\.\\d{1,2})?$";
    public static final String REGEX_PATTERN_NUMBER = "-?[0-9]+(\\\\\\\\.[0-9]+)?";
    public static final String REGEX_PATTERN_SLASH = "\\+";
    public static final String REGEX_PATTERN_ALL_WORD_IN_ENGLISH = "\\W+";
    public static final String REGEX_PATTERN_DATE = "^(?:(?!0000)\\d{4}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1\\d|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\131)|(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)0?2\\229)$";
    public static final String REGEX_PATTERN_DATE_WITH_SLASH = "^(?:(?!0000)\\d{4}(/?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1\\d|2[0-8])|(?:0?[13-9]|1[0-2])\\1(?:29|30)|(?:0?[13578]|1[02])\\131)|(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)(/?)0?2\\229)$";
}
