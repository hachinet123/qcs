package com.tre.centralkitchen.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.http.HttpStatus;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.csv.CSVBean;
import com.tre.centralkitchen.common.utils.FtpUtil;
import com.tre.centralkitchen.domain.bo.system.GetActualProductionBo;
import com.tre.centralkitchen.domain.dto.MtLcu7controlDto;
import com.tre.centralkitchen.domain.po.TrProducePlan;
import com.tre.centralkitchen.domain.po.WkRecievelcuProduced;
import com.tre.centralkitchen.service.IFTPService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tre.centralkitchen.common.constant.LCUConstants.*;

@Service
@Slf4j
public class FTPServiceImpl implements IFTPService {
    @Value(value = "${ck-system-business.bkup-file-path:/tmp}")
    private String backupPath;
    @Value(value = "${ck-system-business.send-file-path:/tmp}")
    private String sndFilePath;
    @Value(value = "${ck-system-business.recv-file-path:/tmp}")
    private String rcvFilePath;

    @Override
    public List<WkRecievelcuProduced> receiveData(MtLcu7controlDto dto, List<GetActualProductionBo> bos, List<TrProducePlan> trProducePlans) throws IOException, InterruptedException {

        sendLCUFile(dto, bos);

        getLCUFile(dto, bos, trProducePlans);

        backUpLCUFile(dto);

        return getWkRecieveLCUProduceds(dto, bos);
    }

    @Override
    public void sendLCUFile(MtLcu7controlDto dto, List<GetActualProductionBo> bos) throws IOException {
        String sndFileDir = sndFilePath + File.separator + dto.getCenterCd();
        String sndNameHST = dto.getSendFilename().substring(0, dto.getSendFilename().lastIndexOf(".")) + HST_SUFFIX;
        String sndNameTXT = dto.getSendFilename().substring(0, dto.getSendFilename().lastIndexOf(".")) + TXT_SUFFIX;
        FileUtil.mkdir(sndFileDir);

        FileUtil.del(sndFileDir + File.separator + sndNameHST);

        StringBuilder getActualCmd = new StringBuilder();
        for (GetActualProductionBo bo : bos) {
            getActualCmd.append(GET_ACTUAL_CMD_FRONT).append(String.format("%02d", bo.getMailNo())).append(GET_ACTUAL_CMD_END);
        }
        File file = FileUtil.writeUtf8String(getActualCmd.toString(), sndFileDir + File.separator + sndNameHST);

        FtpUtil.uploadFile(dto.getIp(), dto.getUser(), dto.getPwd(), dto.getPath(), "", sndNameHST, new FileInputStream(file));

        FtpUtil.renameFile(dto.getIp(), dto.getUser(), dto.getPwd(), dto.getPath(), sndNameHST, sndNameTXT);
    }

    @Override
    public void getLCUFile(MtLcu7controlDto dto, List<GetActualProductionBo> bos, List<TrProducePlan> trProducePlans) throws IOException, InterruptedException {
        String rcvFileDir = rcvFilePath + File.separator + dto.getCenterCd();
        FileUtil.mkdir(rcvFileDir);
        String mailNo = bos.get(0).getMailNo().toString();

        FileUtil.del(rcvFileDir + File.separator + dto.getRecvFilename());
        int cnt = 0;
        while (!FtpUtil.existFile(dto.getIp(), dto.getUser(), dto.getPwd(), dto.getPath(), dto.getRecvFilename())) {
            Thread.sleep(THREAD_SLEEP_TIME);
            cnt++;
            if (cnt > FILE_LOOP_TIME) {
                if (trProducePlans.isEmpty()) {
                    String emptyName = dto.getRecvFilename().substring(0, dto.getRecvFilename().lastIndexOf(".")) + "_" + mailNo + CSV_SUFFIX;
                    FileUtil.writeUtf8String(CSV_HEADER, rcvFileDir + File.separator + emptyName);
                    FileUtil.writeUtf8String("", rcvFileDir + File.separator + dto.getRecvFilename());
                    log.info("null file make path:" + rcvFileDir + File.separator + emptyName + ",File:" + emptyName);
                    return;
                }
                throw new FileNotFoundException();
            }
        }

        log.info("downloadFile Path:" + rcvFileDir + ",File:" + dto.getRecvFilename());
        boolean dFlg = FtpUtil.downloadFile(dto.getIp(), dto.getUser(), dto.getPwd(), dto.getPath(), dto.getRecvFilename(), rcvFileDir);
        if (dFlg) {
            log.info("downloadFile success");
        } else {
            throw new SysBusinessException(SysConstantInfo.FILE_ERROR_DOWNLOAD_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_ERROR_DOWNLOAD_CODE);
        }
        String rcvNewName = dto.getRecvFilename().substring(0, dto.getRecvFilename().lastIndexOf(".")) + "_" + mailNo + CSV_SUFFIX;
        File rcvFile = FileUtil.file(rcvFileDir + File.separator + dto.getRecvFilename());
        if (rcvFile.exists()) {
            log.info("rcvFile exists");
        } else {
            throw new SysBusinessException(SysConstantInfo.FILE_ERROR_CREATE_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_ERROR_CREATE_CODE);
        }

        FileUtil.writeUtf8String(CSV_HEADER, rcvFileDir + File.separator + rcvNewName);
        String s = FileUtil.readUtf8String(rcvFile);
        FileUtil.appendUtf8String(s, rcvFileDir + File.separator + rcvNewName);

        File csvFile = FileUtil.file(rcvFileDir + File.separator + rcvNewName);
        if (csvFile.exists()) {
            log.info("csvFile exists");
        } else {
            throw new SysBusinessException(SysConstantInfo.FILE_ERROR_CREATE_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_ERROR_CREATE_CODE);
        }
    }

    @Override
    public void backUpLCUFile(MtLcu7controlDto dto) throws IOException {

        String bkFileDir = backupPath + File.separator + dto.getCenterCd();
        String rcvFileDir = rcvFilePath + File.separator + dto.getCenterCd();
        FileUtil.mkdir(bkFileDir);
        FileUtil.mkdir(rcvFileDir);

        String timeStamp = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
        String bkNewName = dto.getRecvFilename().substring(0, dto.getRecvFilename().lastIndexOf(".")) + timeStamp + dto.getRecvFilename().substring(dto.getRecvFilename().lastIndexOf("."));
        FileUtil.copy(rcvFileDir + File.separator + dto.getRecvFilename(), bkFileDir + File.separator + bkNewName, true);

    }

    @Override
    public List<WkRecievelcuProduced> getWkRecieveLCUProduceds(MtLcu7controlDto dto, List<GetActualProductionBo> bos) throws IOException {
        String rcvFileDir = rcvFilePath + File.separator + dto.getCenterCd();
        String sndFileDir = sndFilePath + File.separator + dto.getCenterCd();
        FileUtil.mkdir(rcvFileDir);
        FileUtil.mkdir(sndFileDir);

        String mailNo = bos.get(0).getMailNo().toString();
        String rcvNewName = dto.getRecvFilename().substring(0, dto.getRecvFilename().lastIndexOf(".")) + "_" + mailNo + CSV_SUFFIX;
        File csvFile = FileUtil.file(rcvFileDir + File.separator + rcvNewName);
        if (csvFile.exists()) {
            log.info("csvFile exists");
        } else {
            throw new SysBusinessException(SysConstantInfo.FILE_ERROR_CREATE_MSG, HttpStatus.HTTP_OK, SysConstantInfo.FILE_ERROR_CREATE_CODE);
        }
        CsvReader reader = CsvUtil.getReader();
        List<CSVBean> result = reader.read(ResourceUtil.getUtf8Reader(rcvFileDir + File.separator + rcvNewName), CSVBean.class);
        FtpUtil.deleteFile(dto.getIp(), dto.getUser(), dto.getPwd(), dto.getPath(), dto.getRecvFilename());

        List<WkRecievelcuProduced> li = new ArrayList<>();
        result.forEach(csvRow -> {
            WkRecievelcuProduced produced = new WkRecievelcuProduced();
            BeanUtils.copyProperties(csvRow, produced);
            produced.setCenterId(dto.getCenterCd());
            li.add(produced);
        });

        FileUtil.del(sndFileDir + File.separator + dto.getSendFilename());
        FileUtil.del(rcvFileDir + File.separator + rcvNewName);
        return li;
    }

    @Override
    public boolean lcuFileCheck(MtLcu7controlDto dto) {
        String sndNameHST = dto.getSendFilename().substring(0, dto.getSendFilename().lastIndexOf(".")) + HST_SUFFIX;
        String sndNameTXT = dto.getSendFilename().substring(0, dto.getSendFilename().lastIndexOf(".")) + TXT_SUFFIX;
        String rcvNameHST = dto.getRecvFilename().substring(0, dto.getRecvFilename().lastIndexOf(".")) + HST_SUFFIX;
        String rcvNameTXT = dto.getRecvFilename().substring(0, dto.getRecvFilename().lastIndexOf(".")) + TXT_SUFFIX;
        String[] files = new String[]{sndNameHST, sndNameTXT, rcvNameHST, rcvNameTXT};
        if (FtpUtil.existFiles(dto.getIp(), dto.getUser(), dto.getPwd(), dto.getPath(), files)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean lcuFileDelete(MtLcu7controlDto dto) {
        return FtpUtil.deleteFile(dto.getIp(), dto.getUser(), dto.getPwd(), dto.getPath(), dto.getRecvFilename());
    }
}
