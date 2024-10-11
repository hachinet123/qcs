package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.bo.system.ExaminationSearchBo;
import com.tre.centralkitchen.domain.po.MtItemCenterProduct;
import com.tre.centralkitchen.domain.vo.system.MtItemCenterProductVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auther 10116842
 * @date 2023/12/12
 */
@Mapper
@DS("postgres")
public interface ExaminationMapper extends BaseMapperPlus<ExaminationMapper, MtItemCenterProduct, MtItemCenterProductVo> {
    Page<MtItemCenterProductVo> queryPage(Page<MtItemCenterProductVo> page, @Param("param") ExaminationSearchBo bo);

    List<MtItemCenterProductVo> queryPage(@Param("param") ExaminationSearchBo bo);
}
