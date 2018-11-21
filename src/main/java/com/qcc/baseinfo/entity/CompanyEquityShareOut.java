package com.qcc.baseinfo.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompanyEquityShareOut  {

    @JSONField(name = "KeyNo")
    private String KeyNo ;
    @JSONField(name = "CompanyName")
    private String CompanyName ;
    @JSONField(name = "CompanyCode")
    private String CompanyCode ;
    @JSONField(name = "UpdateTime")
    private String UpdateTime;
    @JSONField(name = "DetailCount")
    private int DetailCount ;
    @JSONField(name = "EquityShareDetail")
    private List<EquityShareDetailOut> EquityShareDetail;



    public String getKeyNo() {
        return KeyNo;
    }

    public void setKeyNo(String keyNo) {
        KeyNo = keyNo;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public int getDetailCount() {
        return DetailCount;
    }

    public void setDetailCount(int detailCount) {
        DetailCount = detailCount;
    }

    public List<EquityShareDetailOut> getEquityShareDetail() {
        return EquityShareDetail;
    }

    public void setEquityShareDetail(List<EquityShareDetailOut> equityShareDetail) {
        EquityShareDetail = equityShareDetail;
    }


}
