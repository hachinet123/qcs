package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.Purchasegroupbranches;
import com.tre.centralkitchen.domain.vo.system.PurchasegroupbranchesVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2023-03-02
 */
@Mapper
@DS("postgres")
public interface PurchasegroupbranchesMapper extends BaseMapperPlus<PurchasegroupbranchesMapper, Purchasegroupbranches, PurchasegroupbranchesVo> {

}
