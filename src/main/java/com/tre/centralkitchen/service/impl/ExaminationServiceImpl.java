package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.ExaminationBo;
import com.tre.centralkitchen.domain.bo.system.ExaminationSearchBo;
import com.tre.centralkitchen.domain.po.MtItemCenterProduct;
import com.tre.centralkitchen.domain.vo.system.MtItemCenterProductVo;
import com.tre.centralkitchen.mapper.ExaminationMapper;
import com.tre.centralkitchen.service.ExaminationService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @auther 10116842
 * @date 2023/12/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {
    private final ExaminationMapper mapper;

    @Override
    public TableDataInfo<MtItemCenterProductVo> queryPage(PageQuery pageQuery, ExaminationSearchBo bo) {
        bo.build();
        return TableDataInfo.build(mapper.queryPage(pageQuery.build(), bo));
    }

    @Override
    public void downloadCSV(ExaminationSearchBo bo, HttpServletResponse response) {
        bo.build();
        List<MtItemCenterProductVo> vos = mapper.queryPage(bo);
        SimpleCsvTableUtils.easyCsvExport(response, SysConstants.EXAMINATION_FILE_NAME_CSV,
                vos, MtItemCenterProductVo.class);
    }

    @Override
    public void update(ExaminationBo bo) {
        QueryWrapper<MtItemCenterProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MtItemCenterProduct::getCenterId, bo.getCenterId());
        queryWrapper.lambda().eq(MtItemCenterProduct::getItemId, bo.getItemId());
        List<MtItemCenterProduct> mtItemCenterProductList = mapper.selectList(queryWrapper);

        if (mtItemCenterProductList.isEmpty()) {
            throw new SysBusinessException(SysConstantInfo.EXAMINATION_NOT_EXISTENT_MSG, HttpStatus.HTTP_OK,
                    SysConstantInfo.EXAMINATION_NOT_EXISTENT_CODE);
        }

        if (mtItemCenterProductList.stream().anyMatch(a -> a.getFDel() == 0 && a.getTasteQy() == 0)) {
            throw new SysBusinessException(SysConstantInfo.EXAMINATION_EXISTENT_MSG, HttpStatus.HTTP_OK,
                    SysConstantInfo.EXAMINATION_EXISTENT_CODE);
        }

        MtItemCenterProduct mtItemCenterProduct = BeanUtil.toBean(bo, MtItemCenterProduct.class);
        UpdateWrapper<MtItemCenterProduct> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("center_id", bo.getCenterId());
        updateWrapper.eq("item_id", bo.getItemId());
        mtItemCenterProduct.setTasteQy(0);
        mapper.update(mtItemCenterProduct, updateWrapper);
    }

    @Override
    public void delete(ExaminationBo bo) {
        MtItemCenterProduct mtItemCenterProduct = BeanUtil.toBean(bo, MtItemCenterProduct.class);
        UpdateWrapper<MtItemCenterProduct> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("center_id", bo.getCenterId());
        updateWrapper.eq("item_id", bo.getItemId());
        mtItemCenterProduct.setFDel(1);
        mapper.update(mtItemCenterProduct, updateWrapper);
    }
}
