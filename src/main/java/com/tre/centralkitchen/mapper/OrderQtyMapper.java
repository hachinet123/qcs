package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.service.impl.ShWOrderQtyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Mapper
@DS("sqlserver")
public interface OrderQtyMapper {

    List<ShWOrderQtyVo> searchOrderQtyVo(@Param("centerId") Integer centerId, @Param("beginDate") Date beginDate, @Param("endDate")  Date endDate);
}
