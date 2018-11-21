package com.qcc.constant;

public interface  Constants {


    public final static int  ZERO = 0;

    public final static int  QUERY_HBASE_LIMIT = 200;

    //递归限制
    public final static int RECURSION_LEVEL_ONE = 1;
    public final static int  RECURSION_LEVEL_TEN = 10;
    public final static int  RECURSION_LEVEL_SIX = 6;

    //hbase 的表名
    public final static String HBASE_TBALE_NAME_COMPANY_INVEST = "company_invest";
    public final static String HBASE_TBALE_NAME_MONGO_COMPANY  = "mongo_company";
    public final static String HBASE_TBALE_NAME_COMPANY_TOPTEN  = "company_topten";
    //列族
    public final static String HBASE_COLUMN_FAMILY_DATA = "data";
    //字段
    public final static String HBASE_COLUMN_COMPANYNAME = "companyname";
    public final static String HBASE_COLUMN_COMPANYCODE = "companycode";
    public final static String HBASE_COLUMN_KEYNO = "keyno";
    public final static String HBASE_COLUMN_OPER = "oper";
    public final static String HBASE_COLUMN_PARTNERS = "partners";
    public final static String HBASE_COLUMN_TAGS = "tags";
    public final static String HBASE_COLUMN_INVESTS = "invests";
    public final static String HBASE_COLUMN_ECONKIND = "econkind";
    public final static String HBASE_COLUMN_CREDITCODE = "creditcode";
    public final static String HBASE_COLUMN_NO = "no";
    public final static String HBASE_COLUMN_IS_EXPIRED = "isexpired";

    //kafka的TOPIC
    public final static String TOPIC_MONGO_COMPANY_SPARK = "mongo_company_spark";
    //关联对外投资的keyNos
    public final static String TOPIC_RELATION_INVESTKEYNO_SPARK = "relation_investkeyno_spark";
    //外投资的层级关系keyNos
    public final static String TOPIC_RELATION_PARTNERSKEYNO_SPARK = "relation_partnerskeyno_spark";
}
