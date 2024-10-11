package com.tre.centralkitchen.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 分割リスト
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DivideEquallyUtils {
    public static <T> List<List<T>> fixedGrouping(List<T> source, int n) {

        if (null == source || source.size() == 0 || n <= 0)
            return null;
        List<List<T>> result = new ArrayList<>();

        int sourceSize = source.size();
        int size = (source.size() / n) + 1;
        for (int i = 0; i < size; i++) {
            List<T> subset = new ArrayList<>();
            for (int j = i * n; j < (i + 1) * n; j++) {
                if (j < sourceSize) {
                    subset.add(source.get(j));
                }
            }
            result.add(subset);
        }
        return result;
    }
}
