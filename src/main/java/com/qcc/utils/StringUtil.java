package com.qcc.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;

public class StringUtil {
    private static Logger logger = Logger.getLogger(StringUtil.class);

    public static Double stringToDouble(String  number) {
        Double stockPercent = null;
        String stockPercentString = null;
        if (StringUtils.isNotBlank(number) && !"".equals(number)) {
            NumberFormat numberFormat = NumberFormat.getPercentInstance();
            try {
                System.out.println("number==" + number);
                Double stockPercentNumberFormat = (Double) numberFormat.parse(number);
                DecimalFormat dFormat = new DecimalFormat("#.0000");
                String stockPercentFormat = dFormat.format(stockPercentNumberFormat);
                stockPercent = Double.valueOf(stockPercentFormat);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            stockPercent = null;
        }
        return stockPercent;
    }


    public static Double string2Double(String number) {
        Double intNumber;
        try {
            if (StringUtils.isNotBlank(number) && !"".equals(number)) {
                intNumber = Double.valueOf(number.substring(0, number.indexOf("%")));
            } else {
                intNumber = null;
            }
        }catch (Exception e){
            intNumber = Double.valueOf(0);

        }
        return intNumber;
    }


    /**
     * 是否是个体户
     * @param econKind
     * @param creditCode
     * @param regNo
     * @return
     * @throws Exception
     */
    public static boolean isIndividual(String econKind, String creditCode, String regNo) throws Exception{
        return "个体工商户".equals(CompanyTypeJudy(econKind,creditCode,regNo));
    }


    public static String CompanyTypeJudy(String econKind, String creditCode, String regNo) throws Exception {
        econKind = econKind == null ? "" : econKind;
        creditCode = creditCode == null ? "" : creditCode;
        regNo = regNo == null ? "" : regNo;

        String companyType = econKind;
        if ((econKind.contains("个体") || econKind.contains("个人") || econKind.contains("家庭")) && !econKind.contains("企业")){
            companyType = "个体工商户";
        } else if ((econKind.contains("合作社") || econKind.contains("专业合作")) && !econKind.contains("分支机构")){
            companyType = "农民专业合作经济组织";
        } else if (econKind == null || "".equals(econKind) || econKind.contains("正常") || econKind.contains("其他") || econKind.contains("来料加工")) {
            if ((creditCode.length() == 18 && "2".equals(creditCode.substring(1, 2))) || (regNo.length() == 15 && regNo.substring(6, 7).compareTo("6") > 0 && !"N".equals(regNo.substring(6, 7)))) {
                companyType = "个体工商户";
            } else if ((creditCode.length() == 18 && "3".equals(creditCode.substring(1, 2))) || (regNo.length() == 15 && "N".equals(regNo.substring(6, 7)))) {
                companyType = "农民专业合作经济组织";
            }
        }
        return companyType;
    }


    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

}


