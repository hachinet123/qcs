package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.ExaminationBo;
import com.tre.centralkitchen.domain.bo.system.ExaminationSearchBo;
import com.tre.centralkitchen.domain.bo.system.UserMasterBo;
import com.tre.centralkitchen.domain.vo.system.MtItemCenterProductVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @auther 10116842
 * @date 2023/12/12
 */
public interface ExaminationService {
    TableDataInfo<MtItemCenterProductVo> queryPage(PageQuery pageQuery, ExaminationSearchBo bo);

    void downloadCSV(ExaminationSearchBo bo, HttpServletResponse response);

    void update(ExaminationBo bo);

    void delete(ExaminationBo bo);
}
