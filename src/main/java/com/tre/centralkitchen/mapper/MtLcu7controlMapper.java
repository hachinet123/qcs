package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtLcu7control;
import com.tre.centralkitchen.domain.bo.system.MtLcu7controlBo;
import com.tre.centralkitchen.domain.vo.system.MtLcu7controlVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper interface
 * </p>
 *
 * @author JDev
 * @since 2022-11-17
 */
@Mapper
@DS("postgres")
public interface MtLcu7controlMapper extends BaseMapperPlus<MtLcu7controlMapper, MtLcu7control, MtLcu7controlVo> {
}
