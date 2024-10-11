package com.tre.centralkitchen.common.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {
	public static BigDecimal ifNullSetZero(BigDecimal value) {
		if (value != null) {
			return value;
		} else {
			return BigDecimal.ZERO;
		}
	}
	public static BigDecimal sum(BigDecimal ...value){
		BigDecimal result = BigDecimal.ZERO;
		for (int i = 0; i < value.length; i++){
			result = result.add(ifNullSetZero(value[i]));
		}
		return result;
	}
}