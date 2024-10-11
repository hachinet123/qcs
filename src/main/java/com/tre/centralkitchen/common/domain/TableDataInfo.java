package com.tre.centralkitchen.common.domain;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Table Pagination Data Object
 *
 * @author JDev
 */

@Data
@NoArgsConstructor
@ApiModel("paging response object")
public class TableDataInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * total
     */
    @ApiModelProperty("total")
    private long total;

    /**
     * rows
     */
    @ApiModelProperty("data")
    private List<T> rows;

    /**
     * paging
     *
     * @param list  list data
     * @param total total
     */
    public TableDataInfo(List<T> list, long total) {
        this.rows = list;
        this.total = total;
    }

    public static <T> TableDataInfo<T> build(IPage<T> page) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setRows(page.getRecords());
        rspData.setTotal(page.getTotal());
        return rspData;
    }

    public static <T> TableDataInfo<T> build(List<T> list) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setRows(list);
        rspData.setTotal(list.size());
        return rspData;
    }

    public static <T> TableDataInfo<T> build() {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        return rspData;
    }

}
