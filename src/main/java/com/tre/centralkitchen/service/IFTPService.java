package com.tre.centralkitchen.service;

import com.tre.centralkitchen.domain.bo.system.GetActualProductionBo;
import com.tre.centralkitchen.domain.dto.MtLcu7controlDto;
import com.tre.centralkitchen.domain.po.TrProducePlan;
import com.tre.centralkitchen.domain.po.WkRecievelcuProduced;

import java.io.IOException;
import java.util.List;

public interface IFTPService {
    List<WkRecievelcuProduced> receiveData(MtLcu7controlDto dto, List<GetActualProductionBo> bos, List<TrProducePlan> trProducePlans) throws IOException, InterruptedException;

    void sendLCUFile(MtLcu7controlDto dto, List<GetActualProductionBo> bos) throws IOException;

    void getLCUFile(MtLcu7controlDto dto, List<GetActualProductionBo> bos, List<TrProducePlan> trProducePlans) throws IOException, InterruptedException;

    void backUpLCUFile(MtLcu7controlDto dto) throws IOException;

    List<WkRecievelcuProduced> getWkRecieveLCUProduceds(MtLcu7controlDto dto, List<GetActualProductionBo> bos) throws IOException;

    boolean lcuFileCheck(MtLcu7controlDto dto);

    boolean lcuFileDelete(MtLcu7controlDto dto);
}
