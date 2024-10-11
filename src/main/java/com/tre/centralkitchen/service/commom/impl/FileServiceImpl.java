package com.tre.centralkitchen.service.commom.impl;

import cn.hutool.http.HttpStatus;
import com.tre.centralkitchen.common.constant.FileTypeConstants;
import com.tre.centralkitchen.common.utils.SimpleFmtTableUtils;
import com.tre.centralkitchen.service.commom.FileService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private SimpleFmtTableUtils simpleFmtTableUtils;

    @Override
    public void downloadFmt(String fmtId, HttpServletResponse response) {
        try {
            simpleFmtTableUtils.downloadFmt(response, fmtId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SysBusinessException(e.getMessage(), HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
        }
    }

    @Override
    public void downloadFile(String fileName, HttpServletResponse response) {
        try {
            simpleFmtTableUtils.downloadTempFile(response, fileName, FileTypeConstants.CONTENT_TYPE_PDF);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new SysBusinessException(e.getMessage(), HttpStatus.HTTP_OK, HttpStatus.HTTP_INTERNAL_ERROR);
        }
    }
}
