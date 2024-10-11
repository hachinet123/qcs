package com.tre.centralkitchen.domain.bo.system;

import com.tre.centralkitchen.common.constant.SysConstantInfo;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Data
public class MtItemCenterMaterialSearchBo extends BaseEntityBo {

    @ApiModelProperty(value = "プロセスセンターID", required = true )
    @NotNull(message = SysConstantInfo.CENTER_NOT_EMPTY)
    private Integer centerId;

    @ApiModelProperty(value = "原材料JAN" )
    private String itemId;


    private List<String> itemIds = new LinkedList<>();

    @ApiModelProperty(value = "削除フラグ" )
    private Integer fDel;

    @ApiModelProperty(value = "グループCD" , required = true )
    @NotNull(message = SysConstantInfo.LINE_NOT_EMPTY)
    private String lineId;


    private List<Integer> lineIds = new LinkedList<>();

    @ApiModelProperty(value = "商品名" )
    private String itemName;


    private List<String> itemNames = new LinkedList<>();

    @ApiModelProperty(value = "店舗ID" )
    private Integer storeId;

    @ApiModelProperty(value = "廃盤フラグ" )
    private Integer fActive;

    public void build(){
        if (!this.itemId.equals("")) {
            itemIds.addAll(Arrays.asList(itemId.split(",")));
        }else {
            itemIds=null;
        }
        if (this.lineId != null){
             List<String> Ids = new LinkedList<>();
             Ids.addAll(Arrays.asList(lineId.split(",")));
            for (String id : Ids) {
                lineIds.add(Integer.valueOf(id));
            }
        }
        if (!this.itemName.equals("")){
            itemNames.addAll(Arrays.asList(itemName.split(",")));
        }else {
            itemNames=null;
        }
    }
}
