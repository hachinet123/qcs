package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.domain.po.MtLanguage;
import com.tre.centralkitchen.domain.vo.system.MtLanguageVo;
import com.tre.centralkitchen.mapper.MtLanguagesMapper;
import com.tre.centralkitchen.service.MtLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MtLanguageServiceImpl implements MtLanguageService {

    private final MtLanguagesMapper mapper;

    @Override
    public List<MtLanguage> selectList() {
        return mapper.selectList();
    }
}
