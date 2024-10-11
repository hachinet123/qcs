package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtEmployees;
import com.tre.centralkitchen.domain.vo.system.MtEmployeesVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 従業員基本マスタ Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2023-01-04
 */
@Mapper
@DS("postgres")
public interface MtEmployeesMapper extends BaseMapperPlus<MtEmployeesMapper, MtEmployees, MtEmployeesVo> {
    MtEmployeesVo queryName(@Param("userId") String userId);

    MtEmployeesVo getUserName(@Param("userId") String userId);
}
