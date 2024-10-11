package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.TmShohinBo;
import com.tre.centralkitchen.domain.vo.common.TmShohinVo;
import com.tre.centralkitchen.mapper.TmShohinMapper;
import com.tre.centralkitchen.service.TmShohinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @auther 10116842
 * @date 2023/12/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TmShohinServiceImpl implements TmShohinService {
    private final TmShohinMapper mapper;

    @Override
    public TableDataInfo<TmShohinVo> queryPage(TmShohinBo bo, PageQuery pageQuery) {
        bo.build();
        return TableDataInfo.build(mapper.queryPage(pageQuery.build(), bo));
    }

    @Override
    public void downloadCSV(TmShohinBo bo, HttpServletResponse response) {
        bo.build();
        List<TmShohinVo> vos = mapper.queryPage(bo);
        SimpleCsvTableUtils.easyCsvExport(response, SysConstants.SHOHIN_FILE_NAME_CSV,
                vos, TmShohinVo.class);

    }
}
