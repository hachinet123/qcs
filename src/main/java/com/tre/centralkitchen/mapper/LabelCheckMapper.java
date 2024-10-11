package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.domain.bo.system.LabelCheckBo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author 10253955
 * @since 2023-12-12 8:09
 */
@DS("postgres")
public interface LabelCheckMapper {

    /**
     * ラベルチェックリスト検索
     *
     * @param bo   LabelCheckBo
     * @param build Paging parameter
     * @return Json object of data list and total data
     */
    Page<Map<String,Object>> queryList(@Param("param") LabelCheckBo bo, Page<Object> build);

    /**
     * ラベルチェックリスト検索
     *
     * @param bo LabelCheckBo
     * @return Json object of data list and total data
     */
    List<Map<String, Object>> queryList(@Param("param") LabelCheckBo bo);
}
