package com.tre.centralkitchen.domain.bo.system;

import cn.hutool.core.util.ObjectUtil;
import com.tre.centralkitchen.common.constant.FormatConstants;
import com.tre.centralkitchen.common.domain.BaseEntityBo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Data
public class WarehouseItemPredictionBo extends BaseEntityBo {

    @ApiModelProperty(value = "プロセスセンターID" )
    private Integer centerId;

    @ApiModelProperty(value = "倉庫ID")
    private String wareHouseId;

    private List<Integer> wareHouseIds = new LinkedList<>();

    @ApiModelProperty(value = "原材料JAN")
    private String itemId;

    private List<String> itemIds = new LinkedList<>();

    @ApiModelProperty(value = "倉庫名称")
    private String itemName;

    private List<String> itemNames = new LinkedList<>();

    @ApiModelProperty(value = "グループCD")
    private String lineId;

    private List<Integer> lineIds = new LinkedList<>();

    @DateTimeFormat(pattern = FormatConstants.DATE_FORMAT_08_WITH_01_SEPARATOR_01)
    @ApiModelProperty(value = "基準日")
    private LocalDate date;

    @ApiModelProperty(value = "ベンダーCD")
    private Integer vendorId;

    @ApiModelProperty(value = "ベンダー名")
    private String supplierName;



    public void build() {
        if (!wareHouseId.equals("")) {
            String[] split = wareHouseId.split(",");
            for (String s : split) {
                wareHouseIds.add(Integer.valueOf(s));
            }
        }

        if (!itemId.equals("")) {
                itemIds.addAll(Arrays.asList(itemId.split(",")));

        }

        if (!itemName.equals("")) {
            itemNames.addAll(Arrays.asList(itemName.split(",")));
        }

        if (!lineId.equals("")) {
            String[] split = lineId.split(",");
            for (String s : split) {
                lineIds.add(Integer.valueOf(s));
            }
        }

//        if (ObjectUtil.isNotEmpty(wareHouseId)) {
//            String[] split = wareHouseId.split(",");
//            for (String s : split) {
//                wareHouseIds.add(Integer.valueOf(s));
//            }
//        }
//
//        if (ObjectUtil.isNotEmpty(itemId)) {
//            itemIds.addAll(Arrays.asList(itemId.split(",")));
//
//        }
//
//        if (ObjectUtil.isNotEmpty(itemName)) {
//            itemNames.addAll(Arrays.asList(itemName.split(",")));
//        }
//
//        if (ObjectUtil.isNotEmpty(lineId)) {
//            String[] split = lineId.split(",");
//            for (String s : split) {
//                lineIds.add(Integer.valueOf(s));
//            }
//        }

    }


}
