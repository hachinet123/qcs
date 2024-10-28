package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.domain.po.Lines;
import com.tre.centralkitchen.mapper.LinesMapper;
import com.tre.centralkitchen.service.LinesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinesServiceImpl implements LinesService {

    private final LinesMapper mapper;

    @Override
    public List<Lines> selectList() {
        return mapper.selectList();
    }
}
