package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.vo.system.MailListSearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@DS("postgres")
public interface MailGlanceMapper extends BaseMapperPlus {

    Page<MailListSearchVo> getMailList(@Param("centerId") Integer centerId, Page<Object> build);

    List<MailListSearchVo> getMailList(@Param("centerId") Integer centerId);

}
