package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.GetActualProductionBo;
import com.tre.centralkitchen.domain.bo.system.GetActualProductionSearchBo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface GetActualProductionService {
    void getActualProduction(List<GetActualProductionBo> bos, String userId) throws IOException, InterruptedException;

    TableDataInfo search(GetActualProductionSearchBo bo, PageQuery pageQuery);

    void downloadCSV(GetActualProductionSearchBo bo, HttpServletResponse response);

    void fileSend(GetActualProductionBo bo) throws IOException;

    void fileRecv(GetActualProductionBo bo) throws IOException, InterruptedException;

    void fileBackup(GetActualProductionBo bo) throws IOException;

    void fileRead(GetActualProductionBo bo, String userId) throws IOException;
}
