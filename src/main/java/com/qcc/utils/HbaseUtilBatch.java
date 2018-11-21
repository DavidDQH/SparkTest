package com.qcc.utils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qcc.baseinfo.entity.*;
import com.qcc.constant.Constants;
import jersey.repackaged.com.google.common.base.MoreObjects;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;


/**
 * 链接hbase的工具类
 *
 * @author Administrator
 */
public class HbaseUtilBatch {
    private static Logger logger = Logger.getLogger(HbaseUtilBatch.class);
    private static Configuration conf = HBaseConfiguration.create();
    private static ExecutorService pool = Executors.newScheduledThreadPool(20);
    private static Connection connection = null;
    private static HbaseUtilBatch instance = null;


    private HbaseUtilBatch() {

        /**
         * 创建hbase的数据库链接池
         */
        if (connection == null) {
            try {
//				conf.set("hbase.zookeeper.quorum", "hd-2,hd-3,hd-4");
                conf.set("hbase.zookeeper.quorum", "172.16.20.13,172.16.20.14,172.16.20.15,172.16.20.16,172.16.20.17");
//				conf.set("hbase.zookeeper.quorum", "node02,node03,node04");
                conf.set("hbase.zookeeper.property.clientPort", "2181");
                connection = ConnectionFactory.createConnection(conf, pool);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("hbase connection error!!!");
            }
        }
    }


    /**
     * 获得该类的实例，单例模式
     *
     * @return
     */
    public static synchronized HbaseUtilBatch getInstance() {
        if (instance == null) {
            instance = new HbaseUtilBatch();
        }
        return instance;
    }

    /**
     * 判断Partners的keyNo与元数据节点keyNo是否相等
     * @param partners
     * @param keyNo
     * @return
     */
    public String isPartnersHasParentKeyNo(String partners,String keyNo) {
        String isOrNot = "false";
        List<String> partnersKeyNos= getShareHolderKeyNosByPartners(partners);
        for (String partnersKeyNo : partnersKeyNos){
            if (keyNo.equals(partnersKeyNo)){
                isOrNot = "true";
                break;
            }
        }
        return   isOrNot;
    }

    /**
     * 添加puts集合
     *
     * @param tableName 表名
     * @param puts      要添加的puts实例
     * @throws Exception
     */
    public void addPuts(String tableName, List<Put> puts)
            throws Exception {
        Table table = connection.getTable(TableName.valueOf(tableName));
        table.put(puts);
        table.close();
        System.out.println("成功存入ok!!!");
    }


    /***
     * 用来创建一个put实例
     * @param companyMessagePutHbase
     * @return
     * @throws Exception
     */
    public Put createPutIsExpired(String tableFamily, CompanyMessagePutHbase companyMessagePutHbase) {
        String keyNo = companyMessagePutHbase.getKeyNo();
        Put put = new Put(Bytes.toBytes(keyNo));
        put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_IS_EXPIRED), Bytes.toBytes(MoreObjects.firstNonNull(companyMessagePutHbase.getIsExpired(), "")));
        return put;
    }

    /***
     * 用来创建一个put实例
     * @param companyMessagePutHbase
     * @return
     */
    public Put createPut(String tableName, String tableFamily, CompanyMessagePutHbase companyMessagePutHbase) {
        String keyNo = companyMessagePutHbase.getKeyNo();
        Result result = null;
        try {
            result = qurryHbaseTable(tableName, keyNo);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("查询hbase错误keyNos=" + keyNo + "tableName" + tableName);
        }
        Put put = new Put(Bytes.toBytes(keyNo));
        put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_COMPANYNAME), Bytes.toBytes(MoreObjects.firstNonNull(companyMessagePutHbase.getName(), "")));
        put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_KEYNO), Bytes.toBytes(MoreObjects.firstNonNull(companyMessagePutHbase.getKeyNo(), "")));
        put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_OPER), Bytes.toBytes(MoreObjects.firstNonNull(companyMessagePutHbase.getOper(), "")));
        put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_TAGS), Bytes.toBytes(MoreObjects.firstNonNull(companyMessagePutHbase.getTags(), "")));
        put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_ECONKIND), Bytes.toBytes(MoreObjects.firstNonNull(companyMessagePutHbase.getEconKind(), "")));
        put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_CREDITCODE), Bytes.toBytes(MoreObjects.firstNonNull(companyMessagePutHbase.getCreditCode(), "")));
        put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_NO), Bytes.toBytes(MoreObjects.firstNonNull(companyMessagePutHbase.getNo(), "")));
        put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_IS_EXPIRED), Bytes.toBytes(MoreObjects.firstNonNull(companyMessagePutHbase.getIsExpired(), "")));
        if (null != result && !result.isEmpty()) {
            String partners = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("partners")));
            String companyCode = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("companycode")));
            put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_PARTNERS), Bytes.toBytes(MoreObjects.firstNonNull(partners, "")));
            put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_COMPANYCODE), Bytes.toBytes(MoreObjects.firstNonNull(companyCode, "")));
        } else {
            put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_PARTNERS), Bytes.toBytes(MoreObjects.firstNonNull(companyMessagePutHbase.getPartners(), "")));
            put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_COMPANYCODE), Bytes.toBytes(""));
        }
        return put;
    }

    /**
     * 用来创建一个put实例通过list
     *
     * @param tableFamily
     * @param companyInvestmentDatas
     * @return
     */
    public List<Put> createPutByList(String tableFamily, List<CompanyInvestmentData> companyInvestmentDatas) {
        List<Put> puts = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(companyInvestmentDatas)) {
            for (CompanyInvestmentData companyInvestmentData : companyInvestmentDatas) {
                String keyNO = companyInvestmentData.getKeyNo();
                if (!"".equals(keyNO) && StringUtils.isNotBlank(keyNO)) {
                    Put put = new Put(Bytes.toBytes(keyNO));
                    put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_KEYNO), Bytes.toBytes(MoreObjects.firstNonNull(companyInvestmentData.getKeyNo(), "")));
                    put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_INVESTS), Bytes.toBytes(MoreObjects.firstNonNull(JSON.toJSONString(companyInvestmentData.getCompanyInvest()), "")));
                    put.addColumn(Bytes.toBytes(tableFamily), Bytes.toBytes(Constants.HBASE_COLUMN_TAGS), Bytes.toBytes(MoreObjects.firstNonNull((companyInvestmentData.getTags()), "[]")));
                    puts.add(put);
                }
            }
        }
        return puts;
    }


    /**
     * 单条hbase表company_topten数据
     *
     * @param rowkey
     * @param tableName
     * @return
     * @throws IOException
     */
    public Result qurryHbaseTable(String tableName, String rowkey) throws IOException {
        Result result = null;
        if (!"".equals(rowkey) && StringUtils.isNotBlank(rowkey)) {
            Table table = connection.getTable(TableName.valueOf(tableName));// 获取表
            result = table.get(new Get(Bytes.toBytes(rowkey)));//重点在这，直接查getList<Get>
        }
        if (null != null && !result.isEmpty()) {
            String resultKeyNo = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("isexpired")));
            if ("true".equals(resultKeyNo)) {
                result = null;
            }
        }
        return result;
    }

    /**
     * 单条查询遍历获取partners
     *
     * @param keyNo
     * @param tableName
     * @return
     * @throws IOException
     */
    public static String qurryHbaseTableBatch(String tableName, String keyNo) throws IOException {
        String partners = null;
        if (!"".equals(keyNo) && StringUtils.isNotBlank(keyNo)) {
            Table table = connection.getTable(TableName.valueOf(tableName));// 获取表
            Get get = new Get(Bytes.toBytes(keyNo));
            Result result = table.get(get);//重点在这，直接查getList<Get>
            if (null != result && !result.isEmpty()) {
                String resultKeyNo = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("isexpired")));
                if ("false".equals(resultKeyNo)) {
                    partners = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("partners")));
                }
            }
        }
        return partners;
    }


    /**
     * 限制批量查询遍历hbase的数据
     * @param tableName
     * @param rowkeyList
     * @return
     * @throws IOException
     */
    public static Result[] qurryHbaseTableLimitBatch(String tableName, List<String> rowkeyList) throws IOException {
        Result[] results;
        if (CollectionUtils.isEmpty(rowkeyList)) return results = new Result[0];
        List<Result> resultList = new ArrayList<>();
        Table table = connection.getTable(TableName.valueOf(tableName));
        if (rowkeyList.size() > 200) {
            while (true) {
                List<Get> getList = new ArrayList();
                List<String> curRowList = new ArrayList<>();
                curRowList.addAll(rowkeyList);
                if (rowkeyList.size() > 200) {
                    for (int i = 0; i < 200; i++) {
                        String keyNo = curRowList.get(i);
                        Get get = new Get(Bytes.toBytes(keyNo));
                        getList.add(get);
                        rowkeyList.remove(keyNo);
                    }
                    Result[] curResults = new Result[0];//重点在这，直接查getList<Get>
                    try {
                        curResults = table.get(getList);
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("hbase connection error tableName :" + tableName);
                    }

                    if (curResults.length > 0)
                        for(Result curResult : curResults){
                            String resultKeyNo = Bytes.toString(curResult.getValue(Bytes.toBytes("data"), Bytes.toBytes("isexpired")));
                            if ("false".equals(resultKeyNo)) {
                                resultList.addAll(Arrays.asList(curResult));
                            }
                        }

                } else if (rowkeyList.size() > 0) {
                    for (int i = 0; i < rowkeyList.size(); i++) {
                        Get get = new Get(Bytes.toBytes(rowkeyList.get(i)));
                        getList.add(get);
                    }
                    Result[] curResults = table.get(getList);
                    if (curResults.length > 0) {
                        for(Result curResult : curResults){
                            String resultKeyNo = Bytes.toString(curResult.getValue(Bytes.toBytes("data"), Bytes.toBytes("isexpired")));
                            if ("false".equals(resultKeyNo)) {
                                resultList.addAll(Arrays.asList(curResult));
                            }
                        }
                    }
                    break;
                } else break;
            }
        } else {
            List<Get> getList = new ArrayList();
            for (String rowkey : rowkeyList) {//把rowkey加到get里，再把get装到list中
                Get get = new Get(Bytes.toBytes(rowkey));
                getList.add(get);
            }
            Result[] curResults = table.get(getList);//重点在这，直接查getList<Get>
            List<Result> curResultList=Arrays.asList(curResults);//将数组转换为list集合
            if (org.apache.hadoop.hbase.util.CollectionUtils.notEmpty(curResultList)){
                for(Result curResult : curResultList){
                    String resultKeyNo = Bytes.toString(curResult.getValue(Bytes.toBytes("data"), Bytes.toBytes("isexpired")));
                    if ("false".equals(resultKeyNo)) {
                        resultList.addAll(Arrays.asList(curResult));
                    }
                }
            }
        }
        results = resultList.toArray(new Result[resultList.size()]);
        return results;
    }

    /**
     * 迭代查询keyNo
     * @param keyNos
     * @param level
     * @return
     */
    public List<String> queryKeyNos(List<String> keyNos, int level) {
        List<String> results = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(keyNos)) {
            List<String> keyNoRecursions = new ArrayList<>();
            results = queryHbaseInvestGetKeyNoRecursion(keyNos, keyNoRecursions, level);
        }
        return results;
    }

    /**
     * 查询数据得到keyNo
     * @param keyNos
     * @param resultKeyNos
     * @param level
     * @return
     */
    public List<String> queryHbaseInvestGetKeyNoRecursion(List<String> keyNos, List<String> resultKeyNos, int level) {
        List<String> pkeyNOs = new ArrayList<>();
        Result[] resultsInvest = null;
        if (CollectionUtils.isNotEmpty(keyNos)) {
            try {
                resultsInvest = qurryHbaseTableLimitBatch(Constants.HBASE_TBALE_NAME_COMPANY_INVEST, keyNos);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("查询hbase错误keyNos=" + keyNos + "tableName" + Constants.HBASE_TBALE_NAME_COMPANY_INVEST);
            }
        }

        if (null != resultsInvest && resultsInvest.length > Constants.ZERO) {
            //解析json获取九层的数据
            for (Result result : resultsInvest) {
                String companyBaseInfoList = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("invests")));
                System.out.println("companyBaseInfoList==" + companyBaseInfoList);
                if (!"".equals(companyBaseInfoList) && StringUtils.isNotBlank(companyBaseInfoList)) {
                    JSONArray jsonArray = JSONArray.parseArray(companyBaseInfoList);
                    Object[] objects = jsonArray.toArray();
                    JSONObject jsonObj;
                    //封装keyNO
                    for (Object object : objects) {
                        jsonObj = JSONObject.parseObject(object.toString());
                        //pKeyNo--->patners中的keyNo
                        String pKeyNo = (MoreObjects.firstNonNull(jsonObj.get("keyno"), "")).toString();
                        if (!"".equals(pKeyNo) && StringUtils.isNotBlank(pKeyNo)) {
                            pkeyNOs.add(pKeyNo);
                            resultKeyNos.add(pKeyNo);
                        }
                    }
                }
            }
            if (level < Constants.RECURSION_LEVEL_TEN) {
                queryHbaseInvestGetKeyNoRecursion(pkeyNOs, resultKeyNos, ++level);
            }

        }
        return resultKeyNos;
    }

    /**
     * 解析kafka中的partners的数据的股东
     * @param partners
     * @return
     */
    public List<CompanyInvestRequiredData> getShareHolderDataByPartners(String partners) {
        List<CompanyInvestRequiredData> companyInvestKeyNoAndPercents = new LinkedList<>();
        if (!"".equals(partners) && StringUtils.isNotBlank(partners)) {
            JSONArray jsonArray = JSONArray.parseArray(partners);
            Object[] objects = jsonArray.toArray();
            for (Object object : objects) {
                JSONObject jsonObj = JSONObject.parseObject(object.toString());
                //pKeyNo--->patners中的keyNo
                String pKeyNo = (MoreObjects.firstNonNull(jsonObj.get("KeyNo"), "")).toString();
                String name = (MoreObjects.firstNonNull(jsonObj.get("StockName"), "")).toString();
                String percent = (MoreObjects.firstNonNull(jsonObj.get("StockPercent"), "")).toString();
                String shouldCapi = (MoreObjects.firstNonNull(jsonObj.get("ShouldCapi"), "")).toString();
                String stockRightNum = (MoreObjects.firstNonNull(jsonObj.get("StockRightNum"), "")).toString();
                if (StringUtils.isNotBlank(pKeyNo) && pKeyNo.length() == 32 && !pKeyNo.startsWith("p")) {
                    CompanyInvestRequiredData companyInvestKeyNoAndPercent = new CompanyInvestRequiredData();
                    companyInvestKeyNoAndPercent.setKeyNo(pKeyNo);
                    companyInvestKeyNoAndPercent.setPercent(percent);
                    companyInvestKeyNoAndPercent.setShouldCapi(shouldCapi);
                    companyInvestKeyNoAndPercent.setName(name);
                    companyInvestKeyNoAndPercent.setStockRightNum(stockRightNum);
                    companyInvestKeyNoAndPercents.add(companyInvestKeyNoAndPercent);
                }
            }
        }
        return companyInvestKeyNoAndPercents;
    }

    /**
     * 解析kafka中的partners的数据的股东
     * @param partners
     * @return
     */
    public List<String> getShareHolderKeyNosByPartners(String partners) {
        List<String> shareHolderKeyNos = new ArrayList<>();
        if (!"".equals(partners) && StringUtils.isNotBlank(partners)) {
            JSONArray jsonArray = JSONArray.parseArray(partners);
            Object[] objects = jsonArray.toArray();
            for (Object object : objects) {
                JSONObject jsonObj = JSONObject.parseObject(object.toString());
                //pKeyNo--->patners中的keyNo
                String pKeyNo = (MoreObjects.firstNonNull(jsonObj.get("KeyNo"), "")).toString();
                if (StringUtils.isNotBlank(pKeyNo) && pKeyNo.length() == 32 && !pKeyNo.startsWith("p")) {
                    shareHolderKeyNos.add(pKeyNo);
                }
            }
        }
        return shareHolderKeyNos;
    }

    /**
     * 对比新旧数据的变化
     * @param newShareHolderKeyNosAndPercents
     * @param oldShareHolderKeyNosAndPercents
     * @param keyNo
     * @param name
     * @param newCompanyCode
     * @return
     */
    public List<CompanyInvestmentData> queryHbaseRevampInvest(List<CompanyInvestRequiredData> newShareHolderKeyNosAndPercents,
                                                              List<CompanyInvestRequiredData> oldShareHolderKeyNosAndPercents, String keyNo, String name, String newCompanyCode) {
        List<CompanyInvestmentData> companyInvestmentDatas = new ArrayList<>();

        if (!CollectionUtils.isNotEmpty(newShareHolderKeyNosAndPercents) && !CollectionUtils.isNotEmpty(oldShareHolderKeyNosAndPercents))
            return companyInvestmentDatas;

        //考虑新旧数据的问题,新为空,或者旧为空
        if (CollectionUtils.isNotEmpty(oldShareHolderKeyNosAndPercents) && CollectionUtils.isNotEmpty(newShareHolderKeyNosAndPercents)) {
            List<CompanyInvestRequiredData> tempOldList = new ArrayList<>();
            for (CompanyInvestRequiredData newShareHolderKeyNoAndPercent : newShareHolderKeyNosAndPercents) {
                for (CompanyInvestRequiredData oldShareHolderKeyNoAndPercent : oldShareHolderKeyNosAndPercents) {
                    if ((newShareHolderKeyNoAndPercent.getKeyNo()).equals(oldShareHolderKeyNoAndPercent.getKeyNo())) {
                        if (StringUtils.isBlank(oldShareHolderKeyNoAndPercent.getKeyNo())) {
                            if ((newShareHolderKeyNoAndPercent.getName()).equals(oldShareHolderKeyNoAndPercent.getName())) {
                                tempOldList.add(oldShareHolderKeyNoAndPercent);
                            }
                        } else {
                            tempOldList.add(oldShareHolderKeyNoAndPercent);
                        }

                    }

                }
            }
            //删除相同的keyNO
            oldShareHolderKeyNosAndPercents.removeAll(tempOldList);
        }

        //旧的oldResults去删除keyNo数据
        if (CollectionUtils.isNotEmpty(oldShareHolderKeyNosAndPercents)) {
            queryHbaseDeleteEncapsulateDataByList(companyInvestmentDatas, oldShareHolderKeyNosAndPercents, keyNo, name);
        }

        //新的newResults的keyNo数据
        if (CollectionUtils.isNotEmpty(newShareHolderKeyNosAndPercents)) {
            Map<String, CompanyInvestRequiredData> newShareHolderKeyNosAndPercentsMap = new HashMap<>();
            for (CompanyInvestRequiredData newShareHolderKeyNosAndPercent : newShareHolderKeyNosAndPercents) {
                newShareHolderKeyNosAndPercentsMap.put(newShareHolderKeyNosAndPercent.getKeyNo(), newShareHolderKeyNosAndPercent);
            }
            queryHbaseModifierEncapsulateDataByList(companyInvestmentDatas, newShareHolderKeyNosAndPercentsMap, keyNo, name, newCompanyCode);
        }
        return companyInvestmentDatas;
    }

    /**
     * 新数据过来的parnters的公司股东重新封装
     * @param companyInvestmentDatas
     * @param newShareHolderKeyNosAndPercentsMap
     * @param keyNO
     * @param name
     * @param newCompanyCode
     * @return
     */
    private List<CompanyInvestmentData> queryHbaseModifierEncapsulateDataByList(List<CompanyInvestmentData> companyInvestmentDatas,
                                                                                Map<String, CompanyInvestRequiredData> newShareHolderKeyNosAndPercentsMap, String keyNO, String name, String newCompanyCode) {
        List<String> keyNOs = new ArrayList<>();
        String percent = "";
        String shouldCapi = "";
        String stockRightNum = "";
        String companyCode = "";
        for (Map.Entry<String, CompanyInvestRequiredData> newShareHolderKeyNosAndPercents : newShareHolderKeyNosAndPercentsMap.entrySet()) {
            keyNOs.add(newShareHolderKeyNosAndPercents.getKey());
        }

        Result[] oldResults = new Result[0];
        try {
            oldResults = qurryHbaseTableLimitBatch(Constants.HBASE_TBALE_NAME_COMPANY_INVEST, keyNOs);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("查询hbase错误keyNos=" + keyNOs + "tableName" + Constants.HBASE_TBALE_NAME_MONGO_COMPANY);
        }

        if (null != oldResults && oldResults.length > Constants.ZERO) {
            for (Result result : oldResults) {
                CompanyInvestmentData companyInvestmentData = new CompanyInvestmentData();
                List<CompanyInvest> companyInvests = new ArrayList<>();
                String tags = "[]";

                String companyInvestOfInvests = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("invests")));
                String companyInvestOfKeyNo = MoreObjects.firstNonNull(Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("keyno"))), "");

                System.out.println("companyInvestOfKeyNo===" + companyInvestOfKeyNo);
                //把字符串转为JSONArray处理
                if (!"".equals(companyInvestOfInvests) && StringUtils.isNotBlank(companyInvestOfInvests) && !"".equals(companyInvestOfKeyNo) && StringUtils.isNotBlank(companyInvestOfKeyNo)) {
                    JSONArray jsonArray = JSONArray.parseArray(companyInvestOfInvests);
                    Object[] objects = jsonArray.toArray();
                    JSONObject jsonObj;
                    CompanyInvest companyInvest;
                    //封装keyNO
                    for (Object object : objects) {
                        companyInvest = new CompanyInvest();
                        jsonObj = JSONObject.parseObject(object.toString());
                        //pKeyNo--->patners中的keyNo
                        String investsKeyNo = (MoreObjects.firstNonNull(jsonObj.get("keyno"), "")).toString();
                        String investsName = (MoreObjects.firstNonNull(jsonObj.get("name"), "")).toString();
                        String investsPercent = (MoreObjects.firstNonNull(jsonObj.get("percent"), "")).toString();
                        String investsShouldCapi = (MoreObjects.firstNonNull(jsonObj.get("shouldcapi"), "")).toString();
                        String investsStockRightNum = (MoreObjects.firstNonNull(jsonObj.get("stockrightnum"), "")).toString();
                        String investsCompanyCode = (MoreObjects.firstNonNull(jsonObj.get("companycode"), "")).toString();
                        if (!investsKeyNo.equals(keyNO) && !investsName.equals(name)) {
                            companyInvest.setKeyno(investsKeyNo);
                            companyInvest.setName(investsName);
                            companyInvest.setPercent(investsPercent);
                            companyInvest.setShouldcapi(investsShouldCapi);
                            companyInvest.setStockrightnum(investsStockRightNum);
                            companyInvest.setCompanycode(investsCompanyCode);
                            companyInvests.add(companyInvest);
                        }
                    }

                    companyInvest = new CompanyInvest();
                    companyInvest.setKeyno(keyNO);
                    companyInvest.setName(name);

                    percent = newShareHolderKeyNosAndPercentsMap.get(companyInvestOfKeyNo).getPercent();
                    shouldCapi = newShareHolderKeyNosAndPercentsMap.get(companyInvestOfKeyNo).getShouldCapi();
                    stockRightNum = newShareHolderKeyNosAndPercentsMap.get(companyInvestOfKeyNo).getStockRightNum();

                    Result resultMongoCompany = null;
                    try {
                        resultMongoCompany = qurryHbaseTable(Constants.HBASE_TBALE_NAME_MONGO_COMPANY, companyInvestOfKeyNo);
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("查询hbase错误keyNos=" + companyInvestOfKeyNo + "tableName" + Constants.HBASE_TBALE_NAME_MONGO_COMPANY);
                    }
                    tags = Bytes.toString(resultMongoCompany.getValue(Bytes.toBytes("data"), Bytes.toBytes("tags")));

                    companyInvest.setPercent(percent);
                    companyInvest.setShouldcapi(shouldCapi);
                    companyInvest.setStockrightnum(stockRightNum);
                    companyInvest.setCompanycode(newCompanyCode);

                    companyInvests.add(companyInvest);
                    companyInvestmentData.setKeyNo(companyInvestOfKeyNo);
                    companyInvestmentData.setTags(tags);
                    companyInvestmentData.setCompanyInvest(companyInvests);

                    companyInvestmentDatas.add(companyInvestmentData);
                } else {
                    companyInvests = new ArrayList<>();
                    companyInvestmentData.setKeyNo(companyInvestOfKeyNo);
                    companyInvestmentData.setCompanyInvest(companyInvests);
                    companyInvestmentData.setTags(tags);
                    companyInvestmentDatas.add(companyInvestmentData);
                }
            }
        }
        return companyInvestmentDatas;
    }

    /**
     * 删除对外投资数据
     * @param companyInvestmentDatas
     * @param oldShareHolderKeyNosAndPercents
     * @param keyNO
     * @param name
     * @return
     */
    public List<CompanyInvestmentData> queryHbaseDeleteEncapsulateDataByList(List<CompanyInvestmentData> companyInvestmentDatas,
                                                                             List<CompanyInvestRequiredData> oldShareHolderKeyNosAndPercents, String keyNO, String name) {
        List<String> keyNOs = new ArrayList<>();

        for (CompanyInvestRequiredData oldShareHolderKeyNosAndPercent : oldShareHolderKeyNosAndPercents) {
            keyNOs.add(oldShareHolderKeyNosAndPercent.getKeyNo());
        }

        queryHbaseEncapsulateDataByKeyNos(companyInvestmentDatas, keyNOs, keyNO, name);

        return companyInvestmentDatas;
    }

    /**
     *  删除对外投资数据
     * @param companyInvestmentDatas
     * @param keyNOs
     * @param keyNO
     * @param name
     */
    public void queryHbaseEncapsulateDataByKeyNos(List<CompanyInvestmentData> companyInvestmentDatas, List<String> keyNOs, String keyNO, String name) {
        if (CollectionUtils.isNotEmpty(keyNOs)) {
            Result[] oldResults = new Result[0];
            try {
                oldResults = qurryHbaseTableLimitBatch(Constants.HBASE_TBALE_NAME_COMPANY_INVEST, keyNOs);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("查询hbase错误keyNos=" + keyNOs + "tableName" + Constants.HBASE_TBALE_NAME_MONGO_COMPANY);
            }
            if (null != oldResults && oldResults.length > Constants.ZERO) {
                for (Result result : oldResults) {
                    CompanyInvestmentData companyInvestmentData = new CompanyInvestmentData();
                    List<CompanyInvest> companyInvests = new ArrayList<>();
                    String tags = "[]";

                    String companyInvestOfInvests = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("invests")));
                    String companyInvestOfKeyNo = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("keyno")));
                    companyInvestmentData.setKeyNo(MoreObjects.firstNonNull(companyInvestOfKeyNo, ""));

                    //把字符串转为JSONArray处理
                    if (!"".equals(companyInvestOfInvests) && StringUtils.isNotBlank(companyInvestOfInvests)) {
                        JSONArray jsonArray = JSONArray.parseArray(companyInvestOfInvests);
                        Object[] objects = jsonArray.toArray();
                        JSONObject jsonObj;
                        //封装keyNO
                        for (Object object : objects) {
                            CompanyInvest companyInvest = new CompanyInvest();
                            jsonObj = JSONObject.parseObject(object.toString());
                            //pKeyNo--->patners中的keyNo
                            String investsKeyNo = (MoreObjects.firstNonNull(jsonObj.get("keyno"), "")).toString();
                            String investsName = (MoreObjects.firstNonNull(jsonObj.get("name"), "")).toString();
                            String investsPercent = (MoreObjects.firstNonNull(jsonObj.get("percent"), "")).toString();
                            String investsShouldCapi = (MoreObjects.firstNonNull(jsonObj.get("shouldcapi"), "")).toString();
                            String investsStockRightNum = (MoreObjects.firstNonNull(jsonObj.get("stockrightnum"), "")).toString();
                            String investsCompanyCode = (MoreObjects.firstNonNull(jsonObj.get("companycode"), "")).toString();
                            if (!investsKeyNo.equals(keyNO) && !investsName.equals(name)) {
                                companyInvest.setKeyno(investsKeyNo);
                                companyInvest.setName(investsName);
                                companyInvest.setPercent(investsPercent);
                                companyInvest.setShouldcapi(investsShouldCapi);
                                companyInvest.setStockrightnum(investsStockRightNum);
                                companyInvest.setCompanycode(investsCompanyCode);
                                companyInvests.add(companyInvest);
                            }
                        }
                        companyInvestmentData.setCompanyInvest(companyInvests);
                        companyInvestmentData.setKeyNo(companyInvestOfKeyNo);
                        companyInvestmentData.setTags(tags);
                        companyInvestmentDatas.add(companyInvestmentData);
                    } else {
                        companyInvests = new ArrayList<>();
                        companyInvestmentData.setCompanyInvest(companyInvests);
                        companyInvestmentData.setKeyNo(companyInvestOfKeyNo);
                        companyInvestmentData.setTags(tags);
                        companyInvestmentDatas.add(companyInvestmentData);
                    }
                }
            }
        }
    }


    /**
     * 迭代查询keyNo获取对外投资
     * @param keyNos
     * @param level
     * @return
     */
    public List<String> queryInvestKeyNos(List<String> keyNos, int level) {
        List<String> results = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(keyNos)) {
            List<String> keyNoRecursions = new ArrayList<>();
            results = queryHbaseMongoCompanyGetKeyNoRecursion(keyNos, keyNoRecursions, level);
        }
        return results;
    }

    /**
     *  查询数据得到股东keyNo  5层以内
     * @param keyNos
     * @param resultKeyNos
     * @param level
     * @return
     */
    public List<String> queryHbaseMongoCompanyGetKeyNoRecursion(List<String> keyNos, List<String> resultKeyNos, int level) {
        List<String> pkeyNOs = new ArrayList<>();
        Result[] resultsInvest = null;
        if (CollectionUtils.isNotEmpty(keyNos)) {
            try {
                resultsInvest = qurryHbaseTableLimitBatch(Constants.HBASE_TBALE_NAME_MONGO_COMPANY, keyNos);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("查询hbase错误keyNos=" + keyNos + "tableName" + Constants.HBASE_TBALE_NAME_MONGO_COMPANY);
            }
        }
        if (null != resultsInvest && resultsInvest.length > Constants.ZERO) {
            //解析json获取五层的数据
            for (Result result : resultsInvest) {
                String companyBaseInfoList = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("partners")));
                if (!"".equals(companyBaseInfoList) && StringUtils.isNotBlank(companyBaseInfoList)) {
                    JSONArray jsonArray = JSONArray.parseArray(companyBaseInfoList);
                    Object[] objects = jsonArray.toArray();
                    JSONObject jsonObj;
                    //封装keyNO
                    for (Object object : objects) {
                        jsonObj = JSONObject.parseObject(object.toString());
                        //pKeyNo--->patners中的keyNo
                        String pKeyNo = (MoreObjects.firstNonNull(jsonObj.get("KeyNo"), "")).toString();
                        if (StringUtils.isNotBlank(pKeyNo) && pKeyNo.length() == 32 && !pKeyNo.startsWith("p")) {
                            pkeyNOs.add(pKeyNo);
                            resultKeyNos.add(pKeyNo);
                        }
                    }
                }
            }
            if (level < Constants.RECURSION_LEVEL_SIX) {
                queryHbaseMongoCompanyGetKeyNoRecursion(pkeyNOs, resultKeyNos, ++level);
            }

        }
        return resultKeyNos;
    }

}
