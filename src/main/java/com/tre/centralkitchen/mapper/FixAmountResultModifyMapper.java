package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultModifyBo;
import com.tre.centralkitchen.domain.po.MtMailcontrol;
import com.tre.centralkitchen.domain.vo.system.FixAmountResultModifyPoVo;
import com.tre.centralkitchen.domain.vo.system.FixAmountResultModifyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 10225441
 */
@Mapper
@DS("postgres")
public interface FixAmountResultModifyMapper {
    Page<FixAmountResultModifyPoVo> selectFixAmountResultModify(Page<FixAmountResultModifyPoVo> page, @Param("param") FixAmountResultModifyBo param, @Param("flag") int flag);

    List<FixAmountResultModifyPoVo> selectFixAmountResultModify(@Param("param") FixAmountResultModifyBo param, @Param("flag") int flag);

    List<MtMailcontrol> selectMailNoWithCenterId(@Param("centerId") Integer centerId);

    void updateFixAmountResultModifyPoBatch(@Param("userId") String userId, @Param("paramList") List<FixAmountResultModifyPoVo> paramList, @Param("funcId") Long funcId, @Param("opeId") Long opeId);

    int updateOrInsertFixAmountResultModifyPoBatch(@Param("centerId") Integer centerId, @Param("userId") String userId, @Param("paramList") List<FixAmountResultModifyVo> paramList, @Param("funcId") Long funcId, @Param("opeId") Long opeId);

    List<FixAmountResultModifyPoVo> selectFixAmountResultModifyBefore(@Param("param") FixAmountResultModifyBo param, @Param("flag") int flag);
}
