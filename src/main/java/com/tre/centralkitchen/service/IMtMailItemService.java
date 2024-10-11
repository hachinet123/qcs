package com.tre.centralkitchen.service;

import com.alibaba.fastjson.JSONObject;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.MtMailItemBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.vo.common.FileBackErrorVo;
import com.tre.centralkitchen.domain.vo.system.MtMailItemVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 便設定個別マスタ
 */
public interface IMtMailItemService {
    TableDataInfo<MtMailItemVo> queryList(MtMailItemBo bo, PageQuery pageQuery);

    void downloadCSV(MtMailItemBo bo, HttpServletResponse response);

    Boolean updateByBo(MtMailItemBo bo);

    void updateSqlserverPcOrderList(MtMailItemBo bo);

    void updateSqlserverOrderQty(MtMailItemBo bo);

    void deleteSqlserverOrderList(MtMailItemBo bo);

    void deleteSqlserverOrderQty(MtMailItemBo bo);

    Boolean deleteWithValidByIds(MtMailItemBo bo, Boolean isValid);

    MtMailItemVo queryByList(MtMailItemBo bo);

    FileBackErrorVo fmtImport(UploadBo bo, MultipartFile multipartFile, HttpServletResponse response) throws Exception;

    JSONObject recordImport(List<MtMailItemVo> list);

    Boolean recordImportCheck(String itemId, Integer storeId, Integer centerId);
}
