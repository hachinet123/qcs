package com.tre.centralkitchen.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author 10253955
 * @since 2023-12-11 16:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("paging response object")
@Builder(toBuilder = true)
public class TableDataInfoExtend {

    /**
     * total
     */
    @ApiModelProperty("total")
    private long total;

    /**
     * rows
     */
    @ApiModelProperty("data")
    private List<Map<String, Object>> rows;

    /**
     * headers
     */
    @ApiModelProperty("header")
    private List<Map<String, String>> headers;

    public static TableDataInfoExtend build(List<Map<String, Object>> records, long total, List<Map<String, String>> header) {
        return TableDataInfoExtend.builder()
                .total(total)
                .rows(records)
                .headers(header)
                .build();
    }
}
