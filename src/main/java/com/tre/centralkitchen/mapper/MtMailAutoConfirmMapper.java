package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtMailAutoConfirm;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * システム自動確定 Mapper 接口
 * </p>
 *
 * @author JDev
 * @since 2023-04-18
 */
@Mapper
@DS("postgres")
public interface MtMailAutoConfirmMapper extends BaseMapperPlus<MtMailAutoConfirmMapper, MtMailAutoConfirm, MtMailAutoConfirm> {

}
