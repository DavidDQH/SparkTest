package com.qcc.baseinfo.entity;

import java.io.Serializable;

public class CompanyInvestRequiredData {

    private String keyNo;

    private String name;

    private String percent;
    //股数
    private String stockRightNum;
    //认缴出资额
    private String shouldCapi;
    //公司代码
    private String companyCode;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStockRightNum() {
        return stockRightNum;
    }

    public void setStockRightNum(String stockRightNum) {
        this.stockRightNum = stockRightNum;
    }

    public String getShouldCapi() {
        return shouldCapi;
    }

    public void setShouldCapi(String shouldCapi) {
        this.shouldCapi = shouldCapi;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getKeyNo() {
        return keyNo;
    }

    public void setKeyNo(String keyNo) {
        this.keyNo = keyNo;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }


}
