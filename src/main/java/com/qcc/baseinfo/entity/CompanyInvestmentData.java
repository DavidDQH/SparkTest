package com.qcc.baseinfo.entity;

import java.io.Serializable;
import java.util.List;

public class CompanyInvestmentData  {

    private String keyNo;

    private List<CompanyInvest> companyInvest;

    private  String tags;


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKeyNo() {
        return keyNo;
    }

    public void setKeyNo(String keyNo) {
        this.keyNo = keyNo;
    }

    public List<CompanyInvest> getCompanyInvest() {
        return companyInvest;
    }

    public void setCompanyInvest(List<CompanyInvest> companyInvest) {
        this.companyInvest = companyInvest;
    }

}
