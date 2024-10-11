package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.AppendedMailNo20Bo;
import com.tre.centralkitchen.domain.bo.system.AppendedSearchBo;
import com.tre.centralkitchen.domain.bo.system.AppendedUpdateBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.vo.common.FileBackErrorVo;
import com.tre.centralkitchen.domain.vo.system.AppendedUpdateVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface AppendedService {
    TableDataInfo<AppendedUpdateVo> queryAppended(PageQuery pageParam, AppendedSearchBo bo);

    void downloadCSV(AppendedSearchBo bo, HttpServletResponse response);

    void update(List<AppendedUpdateBo> boList);

    FileBackErrorVo fmtImport(UploadBo bo, MultipartFile file, Boolean warningCheck, HttpServletResponse response) throws Exception;

    void fmtExport(AppendedSearchBo bo, HttpServletResponse response) throws Exception;

    void saveMailNo20(AppendedMailNo20Bo bo);
}
