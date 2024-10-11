package com.tre.centralkitchen.service.impl;

import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.StringConstants;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.vo.system.MailListSearchVo;
import com.tre.centralkitchen.mapper.MailGlanceMapper;
import com.tre.centralkitchen.service.IMailGlanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailGlanceServiceImpl implements IMailGlanceService {
    @Resource
    private MailGlanceMapper mailGlanceMapper;

    /**
     * 便一覧
     */
    @Override
    public TableDataInfo<MailListSearchVo> getMailList(Integer centerId, PageQuery pageQuery) {
        return TableDataInfo.build(mailGlanceMapper.getMailList(centerId, pageQuery.build()));
    }

    @Override
    public void downloadCSV(Integer centerId, HttpServletResponse response) {
        if (centerId == null) {
            String fileName = SimpleCsvTableUtils.getFileName(MailConstants.MAIL_GENERAL_VIEW_CSV_NAME, FormatConstants.DATE_TIME_FORMAT_14_WITH_NO_SEPARATOR);
            String[] header = SimpleCsvTableUtils.getHeaders(MailConstants.MAIL_GENERAL_VIEW_CSV_HEADER, StringConstants.COMMA);
            SimpleCsvTableUtils.printBeansToRespStream(response, fileName, new ArrayList<>(), MailListSearchVo.class, header);
        } else {
            List<MailListSearchVo> mtMailItemVos = mailGlanceMapper.getMailList(centerId);
            mtMailItemVos.forEach(a -> {
                if (a.getSelGroup() != null && !a.getSelGroup().equals("")) {
                    a.setSelGroup(" " + a.getSelGroup());
                }
            });
            SimpleCsvTableUtils.easyCsvExport(response, MailConstants.MAIL_GENERAL_VIEW_CSV_NAME, mtMailItemVos, MailListSearchVo.class);
        }
    }
}
