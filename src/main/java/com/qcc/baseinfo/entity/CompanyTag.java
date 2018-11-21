package com.qcc.baseinfo.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class CompanyTag {

    // 标签类型（1：新三板2：上市3：融资4：其他）
    @JSONField(name = "Type")
    public String Type;
    // 标签名称
    @JSONField(name = "Name")
    public String Name;
    //简称
    @JSONField(name = "ShortName")
    public String ShortName;
    // 参数字段
    @JSONField(name = "DataExtend")
    public String DataExtend;
    //交易场所编码
    @JSONField(name = "TradingPlaceCode")
    public String TradingPlaceCode;
    // 交易场所
    @JSONField(name = "TradingPlaceName")
    public String TradingPlaceName;


    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getDataExtend() {
        return DataExtend;
    }

    public void setDataExtend(String dataExtend) {
        DataExtend = dataExtend;
    }

    public String getTradingPlaceCode() {
        return TradingPlaceCode;
    }

    public void setTradingPlaceCode(String tradingPlaceCode) {
        TradingPlaceCode = tradingPlaceCode;
    }

    public String getTradingPlaceName() {
        return TradingPlaceName;
    }

    public void setTradingPlaceName(String tradingPlaceName) {
        TradingPlaceName = tradingPlaceName;
    }
}
