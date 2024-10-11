package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.domain.bo.system.WorkReportBo;
import com.tre.centralkitchen.domain.vo.system.WorkReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 作業報告書
 * </p>
 *
 * @author 10253955
 * @since 2023-12-19 13:23
 */
@DS("postgres")
public interface WorkReportMapper {

    /**
     * 作業報告書の検索
     *
     * @param bo    WorkReportBo
     * @param build Paging parameter
     * @return Json object of data list and total data
     */
    Page<WorkReportVo> queryList(@Param("param") WorkReportBo bo, Page<Object> build);

    /**
     * 作業報告書のCSV
     *
     * @param bo    WorkReportBo
     * @return WorkReportVo of data list
     */
    List<WorkReportVo> queryCsvData(@Param("param") WorkReportBo bo);
}
