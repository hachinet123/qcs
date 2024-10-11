package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtCenterstatus;
import com.tre.centralkitchen.domain.vo.system.MtCenterstatusVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * センター別環境設定 Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2022-12-12
 */
@Mapper
@DS("postgres")
public interface MtCenterstatusMapper extends BaseMapperPlus<MtCenterstatusMapper, MtCenterstatus, MtCenterstatusVo> {

}
