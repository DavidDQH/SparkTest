package com.qcc.sparkStreaming2db;

import com.alibaba.fastjson.JSON;
import com.qcc.baseinfo.entity.CompanyInvestRequiredData;
import com.qcc.baseinfo.entity.CompanyInvestmentData;
import com.qcc.baseinfo.entity.CompanyMessagePutHbase;
import com.qcc.constant.Constants;
import com.qcc.utils.HbaseUtilBatch;
import com.qcc.utils.KafkaUtil;
import com.qcc.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.log4j.Logger;
import org.apache.spark.streaming.api.java.JavaInputDStream;

import java.util.ArrayList;
import java.util.List;

/**
 * 公司信息保存到hbase
 *
 * @author Administrator
 */
public class KeyNosToKafka extends BaseSave2DB {
    private static Logger logger = Logger.getLogger(KeyNosToKafka.class);

    public static void main(String[] args) {
        KeyNosToKafka kntk = new KeyNosToKafka();
        //topic
        kntk.process(Constants.TOPIC_MONGO_COMPANY_SPARK);
    }

    /**
     * @param dstream 从kafka读到的stream流
     *                解析保存到hbase
     */
    @Override
    public void save2db(JavaInputDStream<ConsumerRecord<String, byte[]>> dstream) {
        dstream.foreachRDD(rdd -> rdd.foreach((vf) -> {
            String companyData = new String(vf.value(), "UTF-8");
            System.out.println("companyData===" + companyData);
            CompanyMessagePutHbase companyMessagePutHbase;

            try {
                companyMessagePutHbase = JSON.parseObject(companyData, CompanyMessagePutHbase.class);
            } catch (Exception e) {
                logger.error(" !!!dirty data!!!" + companyData);
                return;
            }
            String isExpired = companyMessagePutHbase.getIsExpired();

            if ("".equals(isExpired) && StringUtils.isBlank(isExpired)){
                isExpired  = "true";
            }

            String keyNo = companyMessagePutHbase.getKeyNo();
            String name = companyMessagePutHbase.getName();

            /** 获取HbaseCRUD实例 */
            HbaseUtilBatch instance = HbaseUtilBatch.getInstance();
            String isOrNot  = instance.isPartnersHasParentKeyNo(companyMessagePutHbase.getPartners(),keyNo);

            List<String> rowkeyList = new ArrayList();
            rowkeyList.add(keyNo);

            String partners = instance.qurryHbaseTableBatch(Constants.HBASE_TBALE_NAME_MONGO_COMPANY, keyNo);

            if ("false".equals(isExpired) && "false".equals(isOrNot)) {
                /**更新数据 begin*/
                Put putCompanyInvestIsExpired = instance.createPutIsExpired(Constants.HBASE_COLUMN_FAMILY_DATA, companyMessagePutHbase);
                //添加put实例
                List<Put> putCompanyInvestIsExpireds = new ArrayList<>();
                putCompanyInvestIsExpireds.add(putCompanyInvestIsExpired);
                //更新mongo_company数据
                instance.addPuts(Constants.HBASE_TBALE_NAME_COMPANY_INVEST, putCompanyInvestIsExpireds);

                List<CompanyInvestRequiredData> oldShareHolderKeyNos = new ArrayList<>();
                //查询hbase旧数据
                oldShareHolderKeyNos = instance.getShareHolderDataByPartners(partners);
                for (CompanyInvestRequiredData oldShareHolderKeyNo : oldShareHolderKeyNos) {
                    System.out.println("oldShareHolderKeyNos===" + oldShareHolderKeyNo.getKeyNo());
                }

                /**更新数据 begin*/
                Put putMongoCompany = instance.createPut(Constants.HBASE_TBALE_NAME_COMPANY_TOPTEN, Constants.HBASE_COLUMN_FAMILY_DATA, companyMessagePutHbase);
                //添加put实例
                List<Put> putMongoCompanys = new ArrayList<>();
                putMongoCompanys.add(putMongoCompany);
                //更新mongo_company数据
                instance.addPuts(Constants.HBASE_TBALE_NAME_MONGO_COMPANY, putMongoCompanys);

                Result result = instance.qurryHbaseTable(Constants.HBASE_TBALE_NAME_MONGO_COMPANY, keyNo);
                String newPartners = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("partners")));
                String newCompanyCode = Bytes.toString(result.getValue(Bytes.toBytes("data"), Bytes.toBytes("companycode")));
                //解析数据partners
                List<CompanyInvestRequiredData> newShareHolderKeyNos = instance.getShareHolderDataByPartners(newPartners);

                for (CompanyInvestRequiredData newShareHolderKeyNo : newShareHolderKeyNos) {
                    System.out.println("newShareHolderKeyNos===" + newShareHolderKeyNo.getKeyNo());
                }
                //比对数据是否添加还是更新
                List<CompanyInvestmentData> companyInvestmentDatas = new ArrayList<>();
                companyInvestmentDatas = instance.queryHbaseRevampInvest(newShareHolderKeyNos, oldShareHolderKeyNos, keyNo, name, newCompanyCode);
                System.out.println("companyInvestmentDatas===" + JSON.toJSON(companyInvestmentDatas));

                //更新company_invest数据
                List<Put> putCompanyInvests = instance.createPutByList(Constants.HBASE_COLUMN_FAMILY_DATA, companyInvestmentDatas);
                //更新company_invest数据
                instance.addPuts(Constants.HBASE_TBALE_NAME_COMPANY_INVEST, putCompanyInvests);
            } else if ("true".equals(isExpired) || "true".equals(isOrNot)) {
                /**更新数据 begin*/
                Put put_baseInfo = instance.createPutIsExpired(Constants.HBASE_COLUMN_FAMILY_DATA, companyMessagePutHbase);
                //添加put实例
                List<Put> puts = new ArrayList<>();
                puts.add(put_baseInfo);
                //更新mongo_company数据
                instance.addPuts(Constants.HBASE_TBALE_NAME_COMPANY_INVEST, puts);
                instance.addPuts(Constants.HBASE_TBALE_NAME_MONGO_COMPANY, puts);
                //解析数据partners
                List<String> shareHolderKeyNos = instance.getShareHolderKeyNosByPartners(partners);
                for (String shareHolderKeyNo : shareHolderKeyNos) {
                    System.out.println("newShareHolderKeyNos===" + shareHolderKeyNo);
                }
                List<CompanyInvestmentData> companyInvestmentDatas = new ArrayList<>();
                instance.queryHbaseEncapsulateDataByKeyNos(companyInvestmentDatas, shareHolderKeyNos, keyNo, name);
                System.out.println("companyInvestmentDatas===" + JSON.toJSON(companyInvestmentDatas));

                //更新company_invest数据
                List<Put> put_company_invest = instance.createPutByList(Constants.HBASE_COLUMN_FAMILY_DATA, companyInvestmentDatas);
                //更新company_invest数据
                instance.addPuts(Constants.HBASE_TBALE_NAME_COMPANY_INVEST, put_company_invest);
            }

            //股权结构的keyNo
            List<String> equityShareKeyNos = instance.queryKeyNos(rowkeyList, Constants.RECURSION_LEVEL_ONE);
            equityShareKeyNos.add(keyNo);
            KafkaUtil.getInstance().produce(Constants.TOPIC_RELATION_INVESTKEYNO_SPARK, StringUtil.removeDuplicate(equityShareKeyNos));

            //对外投资keyNo
            List<String> investKeyNos = instance.queryInvestKeyNos(rowkeyList, Constants.RECURSION_LEVEL_ONE);
            investKeyNos.add(keyNo);
            KafkaUtil.getInstance().produce(Constants.TOPIC_RELATION_PARTNERSKEYNO_SPARK, StringUtil.removeDuplicate(investKeyNos));
            System.out.println("**************解析完成************");
        }));

//        	  可以打印所有信息，看下ConsumerRecord的结构
//		dstream.foreachRDD(rdd -> {
//		rdd.foreach(x -> {
//			String res = new String(x.value(),"UTF-8");
//			System.out.println(res);
//		});
//	});
    }
}
