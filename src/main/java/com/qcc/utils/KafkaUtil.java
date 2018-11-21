package com.qcc.utils;


import org.apache.commons.collections.CollectionUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;

public class KafkaUtil {
    private static Logger logger = Logger.getLogger(KafkaUtil.class);
    private final KafkaProducer<String, String> producer;

    private static KafkaUtil instance = null;

    /**
     * 获得该类的实例，单例模式
     *
     * @return
     */
    public static synchronized KafkaUtil getInstance() {
        if (instance == null) {
            instance = new KafkaUtil();
        }
        return instance;
    }

    private KafkaUtil() {
        Properties props = new Properties();
//        props.put("bootstrap.servers", "hd-2:9092,hd-3:9092,hd-4:9092");//xxx服务器ip
        props.put("bootstrap.servers", "172.16.20.201:9192");//xxx服务器ip
        props.put("acks", "all");//所有follower都响应了才认为消息提交成功，即"committed"
        props.put("retries", 0);//retries = MAX 无限重试，直到你意识到出现了问题:)
        props.put("batch.size", 16384);//producer将试图批处理消息记录，以减少请求次数.默认的批量处理消息字节数
        //batch.size当批量的数据大小达到设定值后，就会立即发送，不顾下面的linger.ms
        props.put("linger.ms", 1);//延迟1ms发送，这项设置将通过增加小的延迟来完成--即，不是立即发送一条记录，producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理
        props.put("buffer.memory", 33554432);//producer可以用来缓存数据的内存大小。
        props.put("key.serializer",
                "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>(props);
    }

    public void produce(String topic, List<String> keyNos) {
        if (CollectionUtils.isNotEmpty(keyNos)) {
            try {
                for (String keyNo : keyNos) {
                    producer.send(new ProducerRecord<String, String>(topic, keyNo));
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("kafka Push into data problems ==="+keyNos);
            }
        } else {
            System.out.println("keyNos没有,不需推入");
        }
    }

}

