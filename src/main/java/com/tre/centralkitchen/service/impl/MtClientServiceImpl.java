package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.domain.po.MtClient;
import com.tre.centralkitchen.mapper.MtClientMapper;
import com.tre.centralkitchen.service.MtClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MtClientServiceImpl implements MtClientService {

    private final MtClientMapper mapper;

    @Override
    public List<MtClient> selectList() {
        return mapper.selectList();
    }
}
