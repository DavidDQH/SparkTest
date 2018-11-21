package com.qcc.baseinfo.entity;


public class CompanyInvest {

    private String keyno;

    private String name;

    private String percent;

    private String shouldcapi;

    private String stockrightnum;

    private String companycode;

    public String getShouldcapi() {
        return shouldcapi;
    }

    public void setShouldcapi(String shouldcapi) {
        this.shouldcapi = shouldcapi;
    }

    public String getStockrightnum() {
        return stockrightnum;
    }

    public void setStockrightnum(String stockrightnum) {
        this.stockrightnum = stockrightnum;
    }

    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getKeyno() {
        return keyno;
    }

    public void setKeyno(String keyno) {
        this.keyno = keyno;
    }
}
