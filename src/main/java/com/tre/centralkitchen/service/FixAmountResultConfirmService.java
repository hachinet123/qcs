package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultConfirmBo;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultConfirmSearchBo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface FixAmountResultConfirmService {
    void fixAmountResultConfirm(List<FixAmountResultConfirmBo> bos, String userId);

    TableDataInfo search(FixAmountResultConfirmSearchBo bo, PageQuery pageQuery);

    void downloadCSV(FixAmountResultConfirmSearchBo bo, HttpServletResponse response);
}
