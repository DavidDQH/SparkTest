package com.qcc.baseinfo.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

import static org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.Method.get;

public class EquityShareDetailOut  {
    @JSONField(name = "KeyNo")
    public String KeyNo;
    @JSONField(name = "Name")
    public String StockName ;
    @JSONField(name = "CompanyCode")
    public String companyCode ;
    @JSONField(name = "Percent")
    public String StockPercent ;
    @JSONField(name = "PercentTotal")
    public String percentTotal ;
    @JSONField(name = "Level")
    public int level;
    // 0公司1社会组织2人员
    @JSONField(name = "Org")
    public int Org ;
    @JSONField(name = "ShouldCapi")
    public String ShouldCapi;
    // 持股数
    @JSONField(name = "StockRightNum")
    public String StockRightNum;
    @JSONField(name = "DetailCount")
    public int DetailCount;
    // 标签
    @JSONField(name = "Tags")
    public List<CompanyTag> Tags;
    @JSONField(name = "DetailList")
    public List<EquityShareDetailOut> DetailList;


    public String getKeyNo() {
        return KeyNo;
    }

    public void setKeyNo(String keyNo) {
        KeyNo = keyNo;
    }

    public String getStockName() {
        return StockName;
    }

    public void setStockName(String stockName) {
        StockName = stockName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getStockPercent() {
        return StockPercent;
    }

    public void setStockPercent(String stockPercent) {
        StockPercent = stockPercent;
    }

    public String getPercentTotal() {
        return percentTotal;
    }

    public void setPercentTotal(String percentTotal) {
        this.percentTotal = percentTotal;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getOrg() {
        return Org;
    }

    public void setOrg(int org) {
        Org = org;
    }

    public String getShouldCapi() {
        return ShouldCapi;
    }

    public void setShouldCapi(String shouldCapi) {
        ShouldCapi = shouldCapi;
    }

    public String getStockRightNum() {
        return StockRightNum;
    }

    public void setStockRightNum(String stockRightNum) {
        StockRightNum = stockRightNum;
    }

    public int getDetailCount() {
        return DetailCount;
    }

    public void setDetailCount(int detailCount) {
        DetailCount = detailCount;
    }

    public List<CompanyTag> getTags() {
        return Tags;
    }

    public void setTags(List<CompanyTag> tags) {
        Tags = tags;
    }

    public List<EquityShareDetailOut> getDetailList() {
        return DetailList;
    }

    public void setDetailList(List<EquityShareDetailOut> detailList) {
        DetailList = detailList;
    }
}
