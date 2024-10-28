package com.tre.centralkitchen.service;

import com.tre.centralkitchen.domain.po.MtLanguage;
import com.tre.centralkitchen.domain.vo.system.MtLanguageVo;

import java.util.List;

public interface MtLanguageService {

    List<MtLanguage> selectList();

}
