package com.qcc.sparkStreaming2db;

import com.qcc.baseinfo.entity.*;
import com.qcc.utils.ReadKAFKA2DStream;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;



public abstract class BaseSave2DB {
	public  void process(String topic){
		/** 初始化sparkstreaming环境 */
		JavaStreamingContext ssc = initSparkContext();
		/** 从kafka读数据 */
		JavaInputDStream<ConsumerRecord<String, byte[]>> dstream = ReadKAFKA2DStream.getDstream(ssc,topic);
		/** 保存结果到hbase */
		save2db(dstream);

		/** 启动 */
		ssc.start();
		try {
			ssc.awaitTermination();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ssc.close();
	}


	/** 初始化sparkstreaming环境 */
	public static JavaStreamingContext initSparkContext() {
		SparkConf conf = new SparkConf()
				.setAppName("SparkStreamingInvestKeyNOsToKafka")
				.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
				.registerKryoClasses(new Class[]{CompanyData.class,
						CompanyEquityShareOut.class,
						CompanyInvest.class,
						CompanyInvestRequiredData.class,
						CompanyInvestmentData.class,
						CompanyMessagePutHbase.class,
						CompanyTag.class,
						EquityShareDetailOut.class})
				.set("spark.streaming.backpressure.enabled","true")
				.set("spark.streaming.kafka.maxRatePerPartition","10")
//				.set("spark.shuffle.io.maxRetries","60")
//				.set("spark.shuffle.io.retryWait","60s")
//				.set("spark.storage.memoryFraction","0.5")
//				.set("spark.shuffle.memoryFraction","0.3")
				.set("spark.streaming.stopGracefullyOnShutdown", "true");
//				.setMaster("local");


		JavaSparkContext sc = new JavaSparkContext(conf);
		sc.setLogLevel("ERROR");

		/** 用来保存状态信息 */
		sc.setCheckpointDir("./checkpoint");

		/** 创建 JavaStreamingContext */
		JavaStreamingContext ssc = new JavaStreamingContext(sc, Durations.seconds(3));
		return ssc;
	}

	/**
	 * 保存到数据库
	 * @param dstream
	 */
	public abstract  void save2db(JavaInputDStream<ConsumerRecord<String, byte[]>> dstream);

}
