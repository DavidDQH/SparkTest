package com.qcc.baseinfo.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class CompanyData {
    private String companyCode;
    private String Partners;
    private String Tags;

    public CompanyData() {
        companyCode = "";
        Partners = "";
        Tags = "";
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getPartners() {
        return Partners;
    }

    public void setPartners(String partners) {
        Partners = partners;
    }
}
