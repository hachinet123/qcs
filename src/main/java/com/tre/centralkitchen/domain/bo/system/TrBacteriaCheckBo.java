package com.tre.centralkitchen.domain.bo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel(value = " ")
public class TrBacteriaCheckBo extends BaseEntityBo {


    @ApiModelProperty(value = "管理番号", example = "1,2,3")
    private Integer id;

    @ApiModelProperty(value = "タイトル", example = "1,2,3")
    private String title;

    @ApiModelProperty(value = "プロセスセンターID", example = "1,2,3")
    private Integer centerId;


    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @ApiModelProperty(value = "タイトル", example = " 2023/1/1")
    private LocalDate  beginTime;


    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @ApiModelProperty(value = "タイトル", example = "2023/1/3")
    private LocalDate endTime;



    @ApiModelProperty(value = "菌検査状態区分ID", example = "1,2,3")
    private List<Integer> checkstatTypeid;

    @ApiModelProperty(value = "依頼者")
    private String reqUserName;
}
