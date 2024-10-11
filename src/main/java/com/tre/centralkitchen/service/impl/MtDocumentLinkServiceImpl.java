package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.DocumentLinkSearchBo;
import com.tre.centralkitchen.domain.po.MtDocumentLink;
import com.tre.centralkitchen.domain.vo.system.WkRecievelcuProducedEscVo;
import com.tre.centralkitchen.mapper.MtDocumentLinkMapper;
import com.tre.centralkitchen.service.MtDocumentLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MtDocumentLinkServiceImpl implements MtDocumentLinkService {
    private final MtDocumentLinkMapper mapper;

    @Override
    public List<MtDocumentLink> getDocumentLinkList() {
        return mapper.selectList();
    }

    @Override
    public void downloadCSV(DocumentLinkSearchBo bo, HttpServletResponse response) {
        List<WkRecievelcuProducedEscVo> producedEscVos = new ArrayList<>();
        if (bo.getDateType() == 1) {
            // TODO: 2024/1/12 本日csv下载
        } else {
            bo.build();
            producedEscVos = mapper.downloadCSV(bo);
        }
        SimpleCsvTableUtils.easyCsvExport(response, SysConstants.DOWNLOAD_LINK_FILE_NAME, producedEscVos, WkRecievelcuProducedEscVo.class);
    }
}
