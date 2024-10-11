package com.tre.centralkitchen.mapper;

import com.tre.centralkitchen.domain.bo.system.TrBacteriaItemResultBo;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaResultBo;
import com.tre.centralkitchen.domain.po.EmailReceiver;
import com.tre.centralkitchen.domain.po.TrBacteriaCheckTimeResult;
import com.tre.centralkitchen.domain.vo.system.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrBacteriaCheckResultMapper {

    BacteriaCheckResultVo bacteriaCheckResultSelect(Integer id);

    List<BacteriaCheckResultDateVo> bacteriaCheckItemResultSelect(Integer id);

    void bacteriaCheckItemResultUpdate(@Param("param") List<TrBacteriaItemResultBo> param);

    EmailReceiver selectEmailReceiver();

    List<BacteriaCheckResultPDFVo> selectBacteriaCheckResultPdf(Integer id);

}
