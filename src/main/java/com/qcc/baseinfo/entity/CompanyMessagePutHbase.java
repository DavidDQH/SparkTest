package com.qcc.baseinfo.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class CompanyMessagePutHbase  {


    @JSONField(name = "keyno")
    private String KeyNo;
    @JSONField(name = "name")
    private String Name;
    @JSONField(name = "oper")
    private String Oper;
    @JSONField(name = "partners")
    private String Partners;
    @JSONField(name = "IsExpired")
    private String IsExpired;

    private String Tags;

    private String EconKind;

    private String CreditCode;

    private String No;

    public String getIsExpired() {
        return IsExpired;
    }

    public void setIsExpired(String isExpired) {
        IsExpired = isExpired;
    }

    public String getEconKind() {
        return EconKind;
    }

    public void setEconKind(String econKind) {
        EconKind = econKind;
    }

    public String getCreditCode() {
        return CreditCode;
    }

    public void setCreditCode(String creditCode) {
        CreditCode = creditCode;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getKeyNo() {
        return KeyNo;
    }

    public void setKeyNo(String keyNo) {
        KeyNo = keyNo;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOper() {
        return Oper;
    }

    public void setOper(String oper) {
        Oper = oper;
    }

    public String getPartners() {
        return Partners;
    }

    public void setPartners(String partners) {
        Partners = partners;
    }
}
