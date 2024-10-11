package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.TrOutTransitem;
import com.tre.centralkitchen.domain.vo.system.TrOutTransitemVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 出庫明細トラン Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2022-12-15
 */
@Mapper
@DS("postgres")
public interface TrOutTransitemMapper extends BaseMapperPlus<TrOutTransitemMapper, TrOutTransitem, TrOutTransitemVo> {

}
