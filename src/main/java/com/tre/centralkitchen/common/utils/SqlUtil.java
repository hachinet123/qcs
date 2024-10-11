package com.tre.centralkitchen.common.utils;

import cn.hutool.core.exceptions.UtilException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * SQL operation tool class
 *
 * @author ghhealthWebBackend
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlUtil {

    /**
     * Define common sql keywords
     */
    public static final String SQL_REGEX = "select |insert |delete |update |drop |count |exec |chr |mid |master |truncate |char |and |declare ";

    /**
     * Only letters, numbers, underscores, spaces, commas, and decimal points are supported (multiple field sorting is supported)
     */
    public static final String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * Check characters to prevent injection bypass
     */
    public static String escapeOrderBySql(String value) {
        if (StringUtil.isNotEmpty(value) && !isValidOrderBySql(value)) {
            throw new UtilException("The parameter does not conform to the specification and cannot be queried");
        }
        return value;
    }

    /**
     * Verify that the order by syntax conforms to the specification
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

    /**
     * SQL keyword check
     */
    public static void filterKeyword(String value) {
        if (StringUtil.isEmpty(value)) {
            return;
        }
        String[] sqlKeywords = StringUtil.split(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords) {
            if (StringUtil.indexOfIgnoreCase(value, sqlKeyword) > -1) {
                throw new UtilException("Parameters are at risk of SQL injection");
            }
        }
    }
}