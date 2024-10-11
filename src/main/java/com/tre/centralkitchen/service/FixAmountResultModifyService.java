package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultModifyBo;
import com.tre.centralkitchen.domain.bo.system.FixAmountResultModifyUploadBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.vo.common.FileBackErrorVo;
import com.tre.centralkitchen.domain.vo.system.FixAmountResultModifyPoVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author 10225441
 */
public interface FixAmountResultModifyService {
    TableDataInfo<FixAmountResultModifyPoVo> queryFixAmountResultModify(PageQuery pageParam, FixAmountResultModifyBo param);

    void updateFixAmountResultModify(List<FixAmountResultModifyPoVo> paramList);

    FileBackErrorVo fmtImport(UploadBo bo, MultipartFile multipartFile,Boolean warningCheck, HttpServletResponse response) throws Exception;

    void downloadFixAmountResultModifyCsv(PageQuery pageParam, FixAmountResultModifyBo param, HttpServletResponse response);

    Map<String, Integer> sum(FixAmountResultModifyBo param);

    void add(FixAmountResultModifyUploadBo bo);
}
