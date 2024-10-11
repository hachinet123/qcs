package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.business.MessageConstants;
import com.tre.centralkitchen.common.constant.business.PdfConstants;
import com.tre.centralkitchen.common.enums.FunType;
import com.tre.centralkitchen.common.enums.OpeType;
import com.tre.centralkitchen.common.utils.EmailUtils;
import com.tre.centralkitchen.domain.bo.system.BacteriaTimesNumBo;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaItemResultBo;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaResultBo;
import com.tre.centralkitchen.domain.po.*;
import com.tre.centralkitchen.domain.vo.system.*;
import com.tre.centralkitchen.mapper.TrBacteriaCheckItemResultMapper;
import com.tre.centralkitchen.mapper.TrBacteriaCheckMapper;
import com.tre.centralkitchen.mapper.TrBacteriaCheckResultMapper;
import com.tre.centralkitchen.mapper.TrBacteriaCheckTimeResultMapper;
import com.tre.centralkitchen.service.TrBacteriaCheckResultService;
import com.tre.jdevtemplateboot.common.authority.TokenTakeApart;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrBacteriaCheckResultServiceImpl implements TrBacteriaCheckResultService {

    private final EmailUtils emailUtils;
    private final TrBacteriaCheckResultMapper mapper;
    private final TrBacteriaCheckMapper bacteriaCheckMapper;
    private final TrBacteriaCheckTimeResultMapper timeResultMapper;


    @Override
    public BacteriaCheckResultVo search(Integer id) {
        BacteriaCheckResultVo bacteriaCheckResultVo = mapper.bacteriaCheckResultSelect(id);
        String center = bacteriaCheckResultVo.getCenter();
        if (center == null) {
            bacteriaCheckResultVo.setCenter(bacteriaCheckResultVo.getOthercenter());
        }
        List<BacteriaCheckResultDateVo> bacteriaCheckResultDateVos = mapper.bacteriaCheckItemResultSelect(id);
        Map<Integer, List<BacteriaCheckResultDateVo>> listMap = bacteriaCheckResultDateVos.stream().collect(Collectors.groupingBy(BacteriaCheckResultDateVo::getSeq, Collectors.toList()));
        Set<Map.Entry<Integer, List<BacteriaCheckResultDateVo>>> entries = listMap.entrySet();
        List<BacteriaCheckItemResultVo> Vos = new LinkedList<>();
        for (Map.Entry<Integer, List<BacteriaCheckResultDateVo>> entry : entries) {
            List<BacteriaCheckResultDateVo> list = entry.getValue();
            Map<Integer, String> checkTime = new HashMap<>();
            for (BacteriaCheckResultDateVo bacteriaCheckResultDateVo : list) {
                Integer checkTypeId = bacteriaCheckResultDateVo.getCheckTypeId();
                String qy = bacteriaCheckResultDateVo.getQy();
                checkTime.put(checkTypeId, qy);
            }
            if (list.get(0) != null) {
                BacteriaCheckItemResultVo bacteriaCheckItemResultVo1 = BeanUtil.copyProperties(list.get(0), BacteriaCheckItemResultVo.class);
                bacteriaCheckItemResultVo1.setCheckTime(checkTime);
                Vos.add(bacteriaCheckItemResultVo1);
            }
        }
        bacteriaCheckResultVo.setBacteriaCheckItemResultVos(Vos);
        return bacteriaCheckResultVo;
    }

    @Override
    @Transactional
    public void save(TrBacteriaResultBo bo) {
        Integer id = bo.getId();
        TrBacteriaCheck bacteriaCheck = BeanUtil.toBean(bo, TrBacteriaCheck.class);
        UpdateWrapper<TrBacteriaCheck> wrapper = new UpdateWrapper<>();
        List<TrBacteriaItemResultBo> resultBoList = null;
        if (bo.getTrBacteriaItemResultBo() != null) {
            resultBoList = bo.getTrBacteriaItemResultBo();
        }
        for (TrBacteriaItemResultBo bacteriaItemResultBo : resultBoList) {
            bacteriaItemResultBo.setId(id);
        }
        wrapper.lambda().eq(TrBacteriaCheck::getId, id);
        bacteriaCheckMapper.update(bacteriaCheck, wrapper);
        mapper.bacteriaCheckItemResultUpdate(resultBoList);

        for (TrBacteriaItemResultBo bacteriaItemResultBo : resultBoList) {
            List<BacteriaTimesNumBo> numBos = bacteriaItemResultBo.getNumBos();
            List<TrBacteriaCheckTimeResult> timeResults = new LinkedList<>();
            for (BacteriaTimesNumBo numBo : numBos) {
                TrBacteriaCheckTimeResult bacteriaCheckTimeResult = new TrBacteriaCheckTimeResult();
                Integer checkTimeTypeId = numBo.getCheckTimeTypeId();
                String qy = numBo.getQy();
                bacteriaCheckTimeResult.setId(id);
                bacteriaCheckTimeResult.setSeq(bacteriaItemResultBo.getSeq());
                bacteriaCheckTimeResult.setCheckTimeTypeId(checkTimeTypeId);
                bacteriaCheckTimeResult.setQy(qy);
                timeResults.add(bacteriaCheckTimeResult);
                UpdateWrapper<TrBacteriaCheckTimeResult> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda().eq(TrBacteriaCheckTimeResult::getId, id)
                        .eq(TrBacteriaCheckTimeResult::getSeq, bacteriaItemResultBo.getSeq())
                        .eq(TrBacteriaCheckTimeResult::getCheckTimeTypeId, checkTimeTypeId);
                timeResultMapper.update(bacteriaCheckTimeResult, updateWrapper);
            }

        }
    }

    @Override
    public void sendMail(TrBacteriaResultBo bo) {

        List<TrBacteriaItemResultBo> trBacteriaItemResultBo = bo.getTrBacteriaItemResultBo();
        String determine = "合格";
        for (TrBacteriaItemResultBo bacteriaItemResultBo : trBacteriaItemResultBo) {
            Integer fPassed = bacteriaItemResultBo.getFPassed();
            if (fPassed.equals(1)) {
                determine = "不合格";
                break;
            }
        }
        String msg = String.format(MessageConstants.SEND_MESSAGE_BACTERIA_CHECK_RESULT, bo.getId(), bo.getTitle(), determine);
        String subject = MessageConstants.EMAIL_SUBJECT_BACTERIA_CHECK_RESULT + bo.getId() + "_" + bo.getTitle();
        System.out.println("msg = " + msg);
        EmailReceiver emailReceiver = mapper.selectEmailReceiver();
        String toEmail = emailReceiver.getToEmail();
        String[] email = toEmail.split(";");
        if (emailReceiver.getCcEmail() == null && emailReceiver.getBccEmail() == null) {
            emailUtils.sendEmail(email, subject, msg);
        } else if (emailReceiver.getCcEmail() != null && emailReceiver.getBccEmail() == null) {
            String ccEmails = emailReceiver.getCcEmail();
            String[] ccEmail = ccEmails.split(";");
            emailUtils.sendEmail(email, ccEmail, subject, msg);
        } else {
            String ccEmails = emailReceiver.getCcEmail();
            String[] ccEmail = ccEmails.split(";");
            String bccEmails = emailReceiver.getBccEmail();
            String[] bccEmail = bccEmails.split(";");
            emailUtils.sendEmail(email, ccEmail, bccEmail, subject, msg);
        }


    }

    @Override
    public void bacteriaCheckItemResultPdf(Integer id, HttpServletResponse response) throws Exception {
        List<BacteriaCheckResultPDFVo> bacteriaCheckResultPDFVos = mapper.selectBacteriaCheckResultPdf(id);
        Document document = new Document();
        String times = DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss");
        String PDFName = "微生物検査結果報告書_管理番号_" + times + ".pdf";
        response.setHeader("content-type", "application/pdf");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(PDFName, "utf-8"));

        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        BaseFont baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font fontHead = new Font(baseFont, 36, Font.BOLD);
        Font font = new Font(baseFont, 12, Font.NORMAL);

        Paragraph paragraph1 = new Paragraph();
        Phrase phrase = new Phrase();

        String now = DateUtil.format(LocalDateTime.now(), FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01);
        String[] dates = now.split("/");
        String date = dates[0] + "年" + dates[1] + "月" + dates[2] + "日";

        Chunk unLine1 = new Chunk(PdfConstants.PDF_TITLE, fontHead);
        Chunk unLine2 = new Chunk("              作成日 " + date, font);

        Phrase phrase1 = new Phrase();
        phrase1.add(unLine1);
        phrase1.add(unLine2);

        unLine1.setUnderline(3f, -4f);
        unLine2.setUnderline(0.1f, -4f);
        Paragraph par1 = new Paragraph();
        par1.setSpacingAfter(10);
        par1.add(unLine1);
        par1.add(unLine2);
        document.add(par1);
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingAfter(5f);
        table.setSpacingBefore(5f);
        ArrayList<PdfPRow> rows = table.getRows();
        float[] columnWidths = {1f, 8f, 2f, 2f, 2f, 2f};
        table.setWidths(columnWidths);
        PdfPCell cells1[] = new PdfPCell[6];
        PdfPRow row1 = new PdfPRow(cells1);
        cells1[0] = new PdfPCell(new Paragraph("id", font));
        cells1[1] = new PdfPCell(new Paragraph("検体名", font));
        cells1[2] = new PdfPCell(new Paragraph("一般細菌数", font));
        cells1[3] = new PdfPCell(new Paragraph("大腸菌", font));
        cells1[4] = new PdfPCell(new Paragraph("黄色ブドウ球菌フラグ", font));
        cells1[5] = new PdfPCell(new Paragraph("備考", font));
        setAlignFont(cells1, cells1.length);
        rows.add(row1);
        int i = 0;
        for (BacteriaCheckResultPDFVo vo : bacteriaCheckResultPDFVos) {
            PdfPCell cells2[] = new PdfPCell[6];
            PdfPRow row2 = new PdfPRow(cells2);
            i = i + 1;
            cells2[0] = new PdfPCell(new Paragraph(String.valueOf(i), font));
            cells2[1] = new PdfPCell(new Paragraph(vo.getCheckItem() + "      " + vo.getTempZone() + " " + vo.getCheckDate(), font));
            cells2[2] = new PdfPCell(new Paragraph(vo.getQy(), font));
            cells2[3] = new PdfPCell(new Paragraph(vo.getEcolies(), font));
            cells2[4] = new PdfPCell(new Paragraph(vo.getFStaphylococcus(), font));
            cells2[5] = new PdfPCell(new Paragraph(vo.getMemo(), font));
            setAlignFont(cells2, cells2.length);
            rows.add(row2);
        }
        document.add(table);
        document.close();
        writer.close();
    }

    @Override
    public void saveCheck(TrBacteriaResultBo bo) {

        Integer id = bo.getId();
        TrBacteriaCheck bacteriaCheck = BeanUtil.toBean(bo, TrBacteriaCheck.class);
        UpdateWrapper<TrBacteriaCheck> wrapper = new UpdateWrapper<>();
        List<TrBacteriaItemResultBo> resultBoList = null;
        if (bo.getTrBacteriaItemResultBo() != null) {
            resultBoList = bo.getTrBacteriaItemResultBo();
        }
        for (TrBacteriaItemResultBo bacteriaItemResultBo : resultBoList) {
            bacteriaItemResultBo.setId(id);
        }
        Integer checkStatTypeId = 3;
        List<TrBacteriaItemResultBo> boList = bo.getTrBacteriaItemResultBo();
        List<Integer> passed = boList.stream().map(TrBacteriaItemResultBo::getFPassed).collect(Collectors.toList());
        boolean contains = passed.contains(1);
        if (contains){
            checkStatTypeId = 4;
        }
        bacteriaCheck.setCheckStatTypeId(checkStatTypeId);
        wrapper.lambda().eq(TrBacteriaCheck::getId, id);
        bacteriaCheckMapper.update(bacteriaCheck, wrapper);
        mapper.bacteriaCheckItemResultUpdate(resultBoList);

        for (TrBacteriaItemResultBo bacteriaItemResultBo : resultBoList) {
            List<BacteriaTimesNumBo> numBos = bacteriaItemResultBo.getNumBos();
            List<TrBacteriaCheckTimeResult> timeResults = new LinkedList<>();
            for (BacteriaTimesNumBo numBo : numBos) {
                TrBacteriaCheckTimeResult bacteriaCheckTimeResult = new TrBacteriaCheckTimeResult();
                Integer checkTimeTypeId = numBo.getCheckTimeTypeId();
                String qy = numBo.getQy();
                bacteriaCheckTimeResult.setId(id);
                bacteriaCheckTimeResult.setSeq(bacteriaItemResultBo.getSeq());
                bacteriaCheckTimeResult.setCheckTimeTypeId(checkTimeTypeId);
                bacteriaCheckTimeResult.setQy(qy);
                timeResults.add(bacteriaCheckTimeResult);
                UpdateWrapper<TrBacteriaCheckTimeResult> updateWrapper = new UpdateWrapper<>();
                updateWrapper.lambda().eq(TrBacteriaCheckTimeResult::getId, id)
                        .eq(TrBacteriaCheckTimeResult::getSeq, bacteriaItemResultBo.getSeq())
                        .eq(TrBacteriaCheckTimeResult::getCheckTimeTypeId, checkTimeTypeId);
                timeResultMapper.update(bacteriaCheckTimeResult, updateWrapper);
            }

        }
    }

    private void setAlignFont(PdfPCell[] cells, Integer size) {
        for (Integer i = 0; i < size; i++) {
            cells[i].setHorizontalAlignment(Element.ALIGN_CENTER);
            cells[i].setVerticalAlignment(Element.ALIGN_MIDDLE);
        }

    }

}
