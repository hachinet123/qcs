package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtVariousAutoPrint;
import com.tre.centralkitchen.domain.vo.system.MtVariousAutoPrintVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 10253955
 * @since 2024-01-02 13:37
 */
@DS("postgres")
public interface MtVariousAutoPrintMapper extends BaseMapperPlus<MtVariousAutoPrintMapper, MtVariousAutoPrint, MtVariousAutoPrintVO> {

    List<MtVariousAutoPrintVO> selectAllList(@Param(value = "centerId") Integer centerId, @Param(value = "autoPrintTypeId") Integer autoPrintTypeId);
}
