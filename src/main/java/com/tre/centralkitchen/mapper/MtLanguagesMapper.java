package com.tre.centralkitchen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.tre.centralkitchen.common.mapper.BaseMapperPlus;
import com.tre.centralkitchen.domain.po.MtLanguage;
import com.tre.centralkitchen.domain.vo.system.MtLanguageVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@DS("postgres")
public interface MtLanguagesMapper extends BaseMapperPlus<MtClientMapper, MtLanguage, MtLanguageVo> {

    List<MtLanguage> selectList();

}
