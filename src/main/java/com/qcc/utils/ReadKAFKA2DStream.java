package com.qcc.utils;


import java.util.*;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

public class ReadKAFKA2DStream {
	/**
	 * 从kafka读取数据
	 */
	public static JavaInputDStream<ConsumerRecord<String, byte[]>> getDstream(JavaStreamingContext ssc,String topic){
		/** kafka相关参数 */
		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", "172.16.20.201:9192");
//		kafkaParams.put("bootstrap.servers", "node02:9092,node03:9092,node04:9092");
//		kafkaParams.put("bootstrap.servers", "hd-3:9092");
		kafkaParams.put("key.deserializer", StringDeserializer.class);
		kafkaParams.put("value.deserializer", ByteArrayDeserializer.class);
		kafkaParams.put("group.id", "spark_test");
		//earliest //latest
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", true);
		System.out.println(topic);
		Collection<String> topics = Arrays.asList(topic);


		JavaInputDStream<ConsumerRecord<String, byte[]>> stream = KafkaUtils.createDirectStream(ssc,
				LocationStrategies.PreferConsistent(),
				ConsumerStrategies.<String, byte[]> Subscribe(topics, kafkaParams));
		return stream;
	}

}
