package com.tre.centralkitchen.service;

import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.DailyInventoryBo;
import com.tre.centralkitchen.domain.bo.system.DailyInventorySearchBo;
import com.tre.centralkitchen.domain.bo.system.UploadBo;
import com.tre.centralkitchen.domain.vo.common.FileBackErrorVo;
import com.tre.centralkitchen.domain.vo.system.DailyInventoryVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 在庫_日次棚卸
 * </p>
 *
 * @author 10253955
 * @since 2023-12-22 9:24
 */
public interface DailyInventoryService {

    /**
     * 在庫_日次棚卸の検索
     *
     * @param pageParam Paging parameter
     * @param param     Query parameter
     * @return Json object of data list and total data
     */
    TableDataInfo<DailyInventoryVo> search(PageQuery pageParam, DailyInventorySearchBo param);

    /**
     * 在庫_日次棚卸の棚卸登録
     *
     * @param dataList DailyInventoryVo List
     */
    void update(List<DailyInventoryBo> dataList);

    /**
     * 日次棚卸のPDF出力
     *
     * @param param    Query parameter
     * @param response response
     */
    void downloadDailyInventoryPdf(DailyInventorySearchBo param, HttpServletResponse response);

    /**
     * 日次棚卸のCSV出力
     *
     * @param param    Query parameter
     * @param response response
     */
    void downloadCsvOutput(DailyInventorySearchBo param, HttpServletResponse response);

    /**
     * 日次棚卸のExcel出力
     *
     * @param param    Query parameter
     * @param response response
     */
    void downloadExcelOutput(DailyInventorySearchBo param, HttpServletResponse response) throws IOException;

    /**
     * 日次棚卸のFMT取込
     *
     * @param bo       UploadBo
     * @param file     excel
     * @param response response
     * @return File Back Error
     * @throws Exception Exception
     */
    FileBackErrorVo fmtImport(UploadBo bo, MultipartFile file, HttpServletResponse response) throws Exception;
}
