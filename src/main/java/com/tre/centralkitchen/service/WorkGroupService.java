package com.tre.centralkitchen.service;


import com.tre.centralkitchen.common.domain.PageQuery;
import com.tre.centralkitchen.common.domain.TableDataInfo;
import com.tre.centralkitchen.domain.bo.system.WorkGroupBo;
import com.tre.centralkitchen.domain.vo.system.WorkGroupVo;

import javax.servlet.http.HttpServletResponse;

public interface WorkGroupService {

    TableDataInfo<WorkGroupVo> getWorkGroupList(WorkGroupBo bo, PageQuery pageQuery);

    WorkGroupVo getUpWorkGroup(WorkGroupBo bo);

    void deleteWorkGroup(WorkGroupBo bo);

    void updateWorkGroup(WorkGroupBo bo);

    WorkGroupVo insertBigGroup(WorkGroupBo bo);

    void insertSmallGroup(WorkGroupBo bo);

    void downloadWorkGroup(WorkGroupBo bo, HttpServletResponse response);

}
