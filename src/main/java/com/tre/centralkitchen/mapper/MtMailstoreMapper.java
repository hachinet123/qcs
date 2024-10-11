package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtMailstore;
import com.tre.centralkitchen.domain.vo.system.MtMailstoreVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2022-12-30
 */
@Mapper
@DS("postgres")
public interface MtMailstoreMapper extends BaseMapperPlus<MtMailstoreMapper, MtMailstore, MtMailstoreVo> {

}
