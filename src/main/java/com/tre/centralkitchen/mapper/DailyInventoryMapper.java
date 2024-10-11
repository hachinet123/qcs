package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.DailyInventorySearchBo;
import com.tre.centralkitchen.domain.po.TrStock;
import com.tre.centralkitchen.domain.vo.system.DailyInventoryVo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 在庫_日次棚卸
 * </p>
 *
 * @author 10253955
 * @since 2023-12-22 10:30
 */
@DS("postgres")
public interface DailyInventoryMapper extends BaseMapperPlus<DailyInventoryMapper, TrStock, DailyInventoryVo> {

    /**
     * 在庫_日次棚卸の検索
     *
     * @param bo    DailyInventorySearchBo
     * @param build Paging parameter
     * @return Json object of data list and total data
     */
    Page<DailyInventoryVo> queryList(@Param("param") DailyInventorySearchBo bo, @Param("stockDate") LocalDate stockDate, Page<Object> build);

    /**
     * 在庫_日次棚卸の検索
     *
     * @param bo DailyInventorySearchBo
     * @return Json object of data list and total data
     */
    List<DailyInventoryVo> queryList(@Param("param") DailyInventorySearchBo bo, @Param("stockDate") LocalDate stockDate);
}
