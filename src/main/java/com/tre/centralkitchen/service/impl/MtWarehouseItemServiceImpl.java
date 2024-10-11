package com.tre.centralkitchen.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.constant.business.MailConstants;
import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.common.utils.SimpleCsvTableUtils;
import com.tre.centralkitchen.domain.bo.system.MtWarehouseItemBo;
import com.tre.centralkitchen.domain.bo.system.WareHouseItemBo;
import com.tre.centralkitchen.domain.po.MtWarehouseitem;
import com.tre.centralkitchen.domain.po.TrStock;
import com.tre.centralkitchen.domain.vo.system.MtWarehouseItemVo;
import com.tre.centralkitchen.mapper.MtWarehouseItemMapper;
import com.tre.centralkitchen.mapper.TrStockMapper;
import com.tre.centralkitchen.service.MtWarehouseItemService;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MtWarehouseItemServiceImpl implements MtWarehouseItemService {

    private final MtWarehouseItemMapper mapper;
    private final TrStockMapper trStockMapper;

    @Override
    public TableDataInfo<MtWarehouseItemVo> search(MtWarehouseItemBo mtWarehouseitemBo, PageQuery pageQuery) {
        Page<MtWarehouseItemVo> vo = mapper.search(mtWarehouseitemBo, pageQuery.build());
        return TableDataInfo.build(vo);
    }

    @Override
    public void downloadCSV(MtWarehouseItemBo mtWarehouseitemBo, HttpServletResponse response) {
        List<MtWarehouseItemVo> vo = mapper.search(mtWarehouseitemBo);
        SimpleCsvTableUtils.easyCsvExport(response, MailConstants.MT_WAREHOUSE_ITEM_CSV_NAME, vo, MtWarehouseItemVo.class);
    }

    @Override
    @Transactional
    public void save(WareHouseItemBo bo) {
        MtWarehouseItemVo item = mapper.queryWarehouseItem(bo);
        if (!ObjectUtil.isEmpty(item) && item.getFDel() == 0) {
            throw new SysBusinessException(SysConstantInfo.WAREHOUSEITEM_EXISTENT_MSG);
        }

        MtWarehouseitem mtWarehouseitem = BeanUtil.copyProperties(bo, MtWarehouseitem.class);
        if (!ObjectUtil.isEmpty(item) && item.getFDel() == 1) {
            MtWarehouseitem mtWarehouseitem1 = BeanUtil.toBean(bo, MtWarehouseitem.class);
            QueryWrapper<MtWarehouseitem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("center_id", bo.getCenterId());
            queryWrapper.eq("warehouse_id", bo.getWarehouseId());
            queryWrapper.eq("item_id", bo.getItemId());
            mtWarehouseitem1.setFDel(0);
            mapper.update(mtWarehouseitem1, queryWrapper);
        } else {
            mapper.insert(mtWarehouseitem);
            TrStock trStock = BeanUtil.copyProperties(bo, TrStock.class);
            trStock.setStockDate(LocalDate.now());
            trStock.setQy(new BigDecimal(0));
            trStock.setAm(new BigDecimal(0));
            trStock.setCost(new BigDecimal(0));
            trStockMapper.insert(trStock);
        }
    }

    @Override
    public MtWarehouseItemVo info(WareHouseItemBo bo) {
        return mapper.queryWarehouseItem(bo);
    }

    @Override
    public void update(WareHouseItemBo bo) {
        MtWarehouseitem mtWarehouseitem = BeanUtil.toBean(bo, MtWarehouseitem.class);
        QueryWrapper<MtWarehouseitem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("center_id", bo.getCenterId());
        queryWrapper.eq("warehouse_id", bo.getWarehouseId());
        queryWrapper.eq("item_id", bo.getItemId());
        mapper.update(mtWarehouseitem, queryWrapper);
    }

    @Override
    public void delete(WareHouseItemBo bo) {
        MtWarehouseitem mtWarehouseitem = BeanUtil.toBean(bo, MtWarehouseitem.class);
        UpdateWrapper<MtWarehouseitem> queryWrapper = new UpdateWrapper<>();
        queryWrapper.eq("center_id", bo.getCenterId());
        queryWrapper.eq("warehouse_id", bo.getWarehouseId());
        queryWrapper.eq("item_id", bo.getItemId());
        mtWarehouseitem.setFDel(1);
        mapper.update(mtWarehouseitem, queryWrapper);
    }
}
