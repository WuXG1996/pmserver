package com.highplace.biz.pm.domain.base;

import com.highplace.biz.pm.service.util.ExcelResources;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class Property {
    private Long propertyId;

    private String productInstId;

    private Integer propertyType;

    private String zoneId;

    @NotNull
    //@Length(min = 3, max = 30)
    private String buildingId;

    private String unitId;

    @NotNull
    private String roomId;

    private Integer areaUnit;

    @NotNull
    private Double propertyArea;

    private Double floorArea;

    private String houseType;

    private Integer status;

    private Date createTime;

    private Date modifyTime;

    private String remark;

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public String getProductInstId() {
        return productInstId;
    }

    public void setProductInstId(String productInstId) {
        this.productInstId = productInstId == null ? null : productInstId.trim();
    }

    public Integer getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Integer propertyType) {
        this.propertyType = propertyType;
    }

    @ExcelResources(title="分区名称",order=1)
    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId == null ? null : zoneId.trim();
    }

    @ExcelResources(title="楼号",order=2)
    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId == null ? null : buildingId.trim();
    }

    @ExcelResources(title="单元(座)",order=3)
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId == null ? null : unitId.trim();
    }

    @ExcelResources(title="房号",order=4)
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }

    public Integer getAreaUnit() {
        return areaUnit;
    }

    public void setAreaUnit(Integer areaUnit) {
        this.areaUnit = areaUnit;
    }

    @ExcelResources(title="产权面积",order=5)
    public Double getPropertyArea() {
        return propertyArea;
    }

    public void setPropertyArea(Double propertyArea) {
        this.propertyArea = propertyArea;
    }

    @ExcelResources(title="套内面积",order=6)
    public Double getFloorArea() {
        return floorArea;
    }

    public void setFloorArea(Double floorArea) {
        this.floorArea = floorArea;
    }

    @ExcelResources(title="户型",order=7)
    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType == null ? null : houseType.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @ExcelResources(title="房产状态",order=8)
    public String getStatusDesc() {
        return transferStatusToDesc(getStatus());
    }

    //0:未知 1:未售 2:未装修 3:装修中 4:已入住 5:已出租
    public static String transferStatusToDesc(int status) {

        if (status == 1) return "未售";
        if (status == 2) return "未装修";
        if (status == 3) return "装修中";
        if (status == 4) return "已入住";
        if (status == 5) return "已出租";
        return "未知";
    }
}