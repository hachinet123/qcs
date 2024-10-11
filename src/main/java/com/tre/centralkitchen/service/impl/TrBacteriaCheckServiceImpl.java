package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.constant.business.MessageConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.BeanCopyUtils;
import com.tre.centralkitchen.common.utils.EmailUtils;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaCheckBo;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaCheckItemsBo;
import com.tre.centralkitchen.domain.bo.system.TrBacteriaCheckTimeResultBo;
import com.tre.centralkitchen.domain.po.*;
import com.tre.centralkitchen.domain.vo.system.*;
import com.tre.centralkitchen.mapper.*;
import com.tre.centralkitchen.service.TrBacteriaCheckService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TrBacteriaCheckServiceImpl implements TrBacteriaCheckService {

    private final EmailUtils emailUtils;
    private final TrBacteriaCheckMapper mapper;
    private final TrBacteriaCheckItemMapper itemMapper;
    private final TrBacteriaCheckTimeMapper timeMapper;
    private final TrBacteriaCheckItemResultMapper itemResultMapper;
    private final TrBacteriaCheckTimeResultMapper timeResultMapper;

    @Override
    public TableDataInfo<TrBacteriaCheckVo> search(TrBacteriaCheckBo bo, PageQuery pageQuery) {
        Page<TrBacteriaCheckVo> page = mapper.search(pageQuery.build(), bo);
        return TableDataInfo.build(page);
    }

    @Override
    public void downloadCSV(TrBacteriaCheckBo bo, HttpServletResponse response) {

        List<TrBacteriaCheckCSVVo> records = mapper.selectTrBacteriaCheckCSVVo(bo);

        List<TrBacteriaCheckTimeCheckDateVo> checkDateVoList = mapper.selectTrBacteriacheckTimeCheckDateVo(records);

        for (TrBacteriaCheckCSVVo record : records) {
            String checkDate = "";
            Integer id = record.getId();
            Integer seq = record.getSeq();
            for (TrBacteriaCheckTimeCheckDateVo checkTimeCheckDate : checkDateVoList) {
                if (id.equals(checkTimeCheckDate.getId()) && seq.equals(checkTimeCheckDate.getSeq())) {
                    String date = checkTimeCheckDate.getCheckDate();
                    checkDate = checkDate + date + "/";
                }
            }
            String[] dates = checkDate.split("/");
            String newCheckDate = "";
            for (int i = 0; i < dates.length; i++) {
                if (i == dates.length - 1) {
                    newCheckDate = newCheckDate + dates[i];
                } else {
                    newCheckDate = newCheckDate + dates[i] + "/";
                }
            }
            record.setCheckDate(newCheckDate);
        }
        SimpleCsvTableUtils.easyCsvExport(response, MailConstants.TR_BACTERIA_CHECK_CSV_NAME, records, TrBacteriaCheckCSVVo.class);
    }

    @Override
    @Transactional
    public Integer save(TrBacteriaCheckTimeResultBo bo) {
        Integer id = createId();
        save(id, bo);
        if (ObjectUtil.isNotEmpty(bo.getTrBacteriaCheckItems())) {
            itemSave(id, bo.getTrBacteriaCheckItems());
            timeSave(id, bo.getTrBacteriaCheckItems());
        }
        return id;
    }

    @Transactional
    @Override
    public Integer update(TrBacteriaCheckTimeResultBo bo) {
        bacteriaCheckUpdate(bo);
        if (ObjectUtil.isNotEmpty(bo.getTrBacteriaCheckItems())) {
            itemUpdate(bo);
            timeUpdate(bo);
        }
        return bo.getId();
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        TrBacteriaCheck trBacteriaCheck = mapper.selectTrBacteriaCheck(id);
        if (trBacteriaCheck.getCheckStatTypeId() != 1) {
            throw new SysBusinessException(SysConstantInfo.FILE_DATA_EMPTY_MSG);
        }
        TrBacteriaCheck bacteriaCheck = new TrBacteriaCheck();
        bacteriaCheck.setId(id);
        bacteriaCheck.setFDel(1);

        UpdateWrapper<TrBacteriaCheck> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(TrBacteriaCheck::getId, trBacteriaCheck.getId());
        mapper.update(trBacteriaCheck, wrapper);
        TrBacteriaCheckItem item = new TrBacteriaCheckItem();
        item.setId(id);
        item.setFDel(1);
        itemMapper.updateById(item);

        TrBacteriaCheckTime time = new TrBacteriaCheckTime();
        time.setId(id);
        time.setFDel(1);
        timeMapper.updateById(time);

    }

    @Override
    public BacteriaCheckVo bacteriaCheckItemSelect(Integer id) {
        BacteriaCheckVo bacteriaCheckVo = mapper.selectreqName(id);
        List<BacteriaCheckItemVo> bacteriaCheckItemVoList = mapper.bacteriaCheckItemSelect(id);
        List<TrBacteriaCheckTime> trBacteriaCheckTimes = mapper.bacteriaCheckTimeSelect(id);
        Map<Integer, List<TrBacteriaCheckTime>> listMap = trBacteriaCheckTimes.stream().collect(Collectors.groupingBy(TrBacteriaCheckTime::getSeq));
        for (BacteriaCheckItemVo bacteriaCheckItemVo : bacteriaCheckItemVoList) {
            List<Integer> checkTimeTypeIds = new LinkedList<>();
            if (bacteriaCheckItemVo.getProductDate() != null) {
                String productDate = bacteriaCheckItemVo.getProductDate();
                String[] split = productDate.split(" ");
                String newProductDate = split[0].replace("-", "/");
                bacteriaCheckItemVo.setProductDate(newProductDate);
                String productTime = split[1];
                String[] times = productTime.split(":");

                bacteriaCheckItemVo.setProductTime(times[0]);
            }
            List<TrBacteriaCheckTime> trBacteriaCheckTimes1 = listMap.get(bacteriaCheckItemVo.getSeq());
            for (TrBacteriaCheckTime trBacteriaCheckTime : trBacteriaCheckTimes1) {
                int checktimeTypeid = trBacteriaCheckTime.getChecktimeTypeid();
                checkTimeTypeIds.add(checktimeTypeid);
            }
            bacteriaCheckItemVo.setCheckTimeTypeId(checkTimeTypeIds);
        }
        bacteriaCheckVo.setTrBacteriaCheckItems(bacteriaCheckItemVoList);

        return bacteriaCheckVo;
    }

    @Override
    @Transactional
    public void bacteriaCheckItemResultImport(TrBacteriaCheckTimeResultBo bo) {
        Integer id = bo.getId();
        itemResultMapper.deleteById(id);
        timeResultMapper.deleteById(id);
        List<TrBacteriaCheckItemsBo> trBacteriaCheckItems = bo.getTrBacteriaCheckItems();
        List<TrBacteriaCheckItemResult> itemResult = new ArrayList<>();
        for (TrBacteriaCheckItemsBo item : trBacteriaCheckItems) {
            TrBacteriaCheckItemResult result = BeanCopyUtils.copy(item, TrBacteriaCheckItemResult.class);
            result.setId(id);
            result.setFStaphylococcus(0);
            result.setFPassed(0);
            itemResult.add(result);
        }
        itemResultMapper.insertBatch(itemResult);

        for (TrBacteriaCheckItemsBo item : trBacteriaCheckItems) {
            List<TrBacteriaCheckTimeResult> timeResult = new ArrayList<>();
            List<Integer> checkTimeTypeIds = item.getCheckTimeTypeId();
            for (Integer checkTimeTypeId : checkTimeTypeIds) {
                TrBacteriaCheckTimeResult result = BeanCopyUtils.copy(item, TrBacteriaCheckTimeResult.class);
                result.setId(id);
                result.setCheckTimeTypeId(checkTimeTypeId);
                result.setQy("");
                timeResult.add(result);
            }
            timeResultMapper.insertBatch(timeResult);
        }

        String msg = String.format(MessageConstants.SEND_MESSAGE, id, bo.getTitle(), itemResult.size());
        String subject = MessageConstants.EMAIL_SUBJECT + bo.getId() + "_" + bo.getTitle();
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


    private void timeUpdate(TrBacteriaCheckTimeResultBo bo) {
        Integer id = bo.getId();
        timeMapper.deleteById(id);
        List<TrBacteriaCheckItemsBo> trBacteriaCheckItems = bo.getTrBacteriaCheckItems();
        timeSave(id, trBacteriaCheckItems);

    }

    private void itemUpdate(TrBacteriaCheckTimeResultBo bo) {
        Integer id = bo.getId();
        List<TrBacteriaCheckItemsBo> trBacteriaCheckItems = bo.getTrBacteriaCheckItems();
        itemMapper.deleteById(id);
        itemSave(id, trBacteriaCheckItems);
    }

    private void bacteriaCheckUpdate(TrBacteriaCheckTimeResultBo bo) {
        TrBacteriaCheck trBacteriaCheck = BeanUtil.copyProperties(bo, TrBacteriaCheck.class);
        if (bo.getOtherCenter() != null) {
            trBacteriaCheck.setOtherCenter(bo.getOtherCenter());
        }
        UpdateWrapper<TrBacteriaCheck> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(TrBacteriaCheck::getId, trBacteriaCheck.getId());
        mapper.update(trBacteriaCheck, wrapper);
    }


    private void timeSave(Integer id, List<TrBacteriaCheckItemsBo> bos) {
        for (TrBacteriaCheckItemsBo bo : bos) {
            List<TrBacteriaCheckTime> timeList = new ArrayList<>();
            List<Integer> checkTimeTypeIdList = bo.getCheckTimeTypeId();
            for (Integer checkTimeTypeId : checkTimeTypeIdList) {
                TrBacteriaCheckTime trBacteriaCheckTime = new TrBacteriaCheckTime();
                trBacteriaCheckTime.setId(id);
                trBacteriaCheckTime.setSeq(bo.getSeq());
                trBacteriaCheckTime.setChecktimeTypeid(checkTimeTypeId);
                timeList.add(trBacteriaCheckTime);
            }
            timeMapper.insertBatch(timeList);
        }


    }

    private void itemSave(Integer id, List<TrBacteriaCheckItemsBo> bos) {
        List<TrBacteriaCheckItem> itemList = new ArrayList<>();
        for (TrBacteriaCheckItemsBo bo : bos) {
            TrBacteriaCheckItem trBacteriaCheckItem = BeanUtil.toBean(bo, TrBacteriaCheckItem.class);
            trBacteriaCheckItem.setId(id);
            if (ObjectUtil.isNotEmpty(bo.getOtherCheckDate())) {
                trBacteriaCheckItem.setOtherCheckTime(bo.getOtherCheckDate());
            }
            if (bo.getProductDate() != null && bo.getProductTime() != null) {
                String produceDate = bo.getProductDate() + " " + bo.getProductTime() + ":00:00";
                String format = "yyyy/MM/dd HH:mm:ss";
                LocalDateTime dateTime = LocalDateTime.parse(produceDate, DateTimeFormatter.ofPattern(format));
                trBacteriaCheckItem.setProduceDate(dateTime);
            }else if (bo.getProductDate() != null && bo.getProductTime() == null){
                String produceDate = bo.getProductDate() + " "+"00:00:00";
                String format = "yyyy/MM/dd HH:mm:ss";
                LocalDateTime dateTime = LocalDateTime.parse(produceDate, DateTimeFormatter.ofPattern(format));
                trBacteriaCheckItem.setProduceDate(dateTime);
            }

            itemList.add(trBacteriaCheckItem);
        }
        itemMapper.insertBatch(itemList);
    }

    private void save(Integer id, TrBacteriaCheckTimeResultBo bo) {
        TrBacteriaCheck trBacteriaCheck = BeanUtil.copyProperties(bo, TrBacteriaCheck.class);
        trBacteriaCheck.setId(id);
        trBacteriaCheck.setOtherCenter(bo.getOtherCenter());
        mapper.insert(trBacteriaCheck);
    }


    private Integer createId() {
        String id = "";
        SysparamVo sysparamVo = mapper.createId();
        String paramVal1 = sysparamVo.getParamVal1();
        Integer param1 = Integer.valueOf(paramVal1) + 1;
        mapper.updateParamVal1(param1.toString());
        switch (sysparamVo.getParamVal1().length()) {
            case 1:
                id = sysparamVo.getParamVal2() + "00" + paramVal1;
                break;
            case 2:
                id = sysparamVo.getParamVal2() + "0" + paramVal1;
                break;
            case 3:
                id = sysparamVo.getParamVal2() + "" + paramVal1;
                break;
            default:
                break;
        }
        return Integer.valueOf(id);
    }
}
