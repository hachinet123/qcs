package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.MtItemCenterMaterialBo;
import com.tre.centralkitchen.domain.bo.system.MtItemCenterMaterialSearchBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.vo.common.FileBackErrorVo;
import com.tre.centralkitchen.domain.vo.system.ItemCenterMaterSearchVo;
import com.tre.centralkitchen.domain.vo.system.ItemCenterMaterialVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MtItemCanterMaterialService {

    TableDataInfo<ItemCenterMaterialVo> search(MtItemCenterMaterialSearchBo bo, PageQuery pageQuery);

    void save(MtItemCenterMaterialBo bo);

    void downloadCSV(MtItemCenterMaterialSearchBo bo, HttpServletResponse response);

    ItemCenterMaterSearchVo info(MtItemCenterMaterialBo bo);

    void update(MtItemCenterMaterialBo bo);

    void delete(MtItemCenterMaterialBo bo);

    FileBackErrorVo fmtImport( MultipartFile file, HttpServletResponse response) throws IOException;

    String selectItemName(Integer centerId, String itemId);
}
