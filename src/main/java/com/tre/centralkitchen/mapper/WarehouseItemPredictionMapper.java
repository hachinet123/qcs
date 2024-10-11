package com.tre.centralkitchen.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.domain.bo.system.WarehouseItemPredictionBo;
import com.tre.centralkitchen.domain.vo.system.WarehouseItemPredictionVo;
import com.tre.centralkitchen.domain.vo.system.WarehouseQyVo;
import com.tre.centralkitchen.domain.vo.system.holidayVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Mapper
public interface WarehouseItemPredictionMapper {

    Page<WarehouseItemPredictionVo> search(@Param("bo") WarehouseItemPredictionBo bo, Page<WarehouseItemPredictionVo> build);

    List<WarehouseItemPredictionVo> search(@Param("bo") WarehouseItemPredictionBo bo);

    List<WarehouseQyVo> searchVo(Integer centerId, LocalDate date, LocalDate  endDate);

    List<holidayVo> searchHoliday();
}
