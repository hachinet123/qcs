package com.tre.centralkitchen.common.domain;

import lombok.Data;

import java.util.List;

/**
 *
 * @date 2022-11-28
 */
@Data
public class CustomPageData<T> {

    private Integer total;

    private List<T> rows;

    private Integer size;

    private Integer current;

    private Integer pages;

    private Integer orderSum;
}
