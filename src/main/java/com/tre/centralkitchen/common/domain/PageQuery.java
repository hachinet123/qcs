package com.tre.centralkitchen.common.domain;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.utils.SqlUtil;
import com.tre.centralkitchen.common.utils.StringUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Paging query entity class
 *
 * @author Lion Li
 */

@Data
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Paging Size
     */
    @ApiModelProperty("Paging Size")
    private Integer pageSize;

    /**
     * current page number
     */
    @ApiModelProperty("current page number")
    private Integer pageNum;

    /**
     * sort column
     */
    @ApiModelProperty("sort column")
    private String orderByColumn;

    /**
     * sorting direction desc or asc
     */
    @ApiModelProperty(value = "sorting direction", example = "asc,desc")
    private String isAsc;

    /**
     * Current record start index Default value
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * The number of records displayed on each page Default value Default search all
     */
    public static final int DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;

    public <T> Page<T> build() {
        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        Page<T> page = new Page<>(pageNum, pageSize);
        OrderItem orderItem = buildOrderItem();
        if (ObjectUtil.isNotNull(orderItem)) {
            page.addOrder(orderItem);
        }
        return page;
    }

    private OrderItem buildOrderItem() {
        // Compatible front-end sorting types
        if ("ascending".equals(isAsc)) {
            isAsc = "asc";
        } else if ("descending".equals(isAsc)) {
            isAsc = "desc";
        }
        if (StringUtil.isNotBlank(orderByColumn)) {
            String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
            orderBy = StringUtil.toUnderScoreCase(orderBy);
            if ("asc".equals(isAsc)) {
                return OrderItem.asc(orderBy);
            } else if ("desc".equals(isAsc)) {
                return OrderItem.desc(orderBy);
            }
        }
        return null;
    }

}
