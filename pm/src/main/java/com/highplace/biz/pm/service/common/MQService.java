package com.highplace.biz.pm.service.common;

import com.alibaba.fastjson.JSON;
import com.highplace.biz.pm.config.MQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//提供消息发送和消息处理服务
@Component
public class MQService {

    public static final Logger logger = LoggerFactory.getLogger(MQService.class);

    public static String MSG_KEY_MSGID = "M_msgId";
    public static String MSG_KEY_PRODUCTINSTID = "M_productInstId";
    public static String MSG_KEY_FILEURL = "M_fileUrl";
    public static String MSG_KEY_TARGET = "M_target";
    public static String MSG_KEY_VENDOR = "M_vendor";
    public static String MSG_KEY_CHARGE_ID = "M_chargeId";

    @Autowired
    private AmqpTemplate mqTemplate;

    @Autowired
    private MQServiceHandler mqServiceHandler;


    //发送import消息
    public void sendImportMessage(String msgId, String productInstId, String target, String fileUrl, Integer vendor, Map<String, Object> extraMap ) {

        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put(MSG_KEY_MSGID, msgId);
        msgMap.put(MSG_KEY_PRODUCTINSTID, productInstId);
        msgMap.put(MSG_KEY_FILEURL, fileUrl);
        msgMap.put(MSG_KEY_TARGET, target);
        msgMap.put(MSG_KEY_VENDOR, vendor);

        if(extraMap != null ) msgMap.putAll(extraMap);  //加入额外数据

        String msg = JSON.toJSONString(msgMap);
        logger.debug("Send MQ batchImport message: {}" , msg);
        //发送到消息队列
        mqTemplate.convertAndSend(MQConfig.BATCH_IMPORT_QUEUE_NAME, msg);
    }

    //发送export消息
    public void sendExportMessage(String msgId, String productInstId, String target, Integer vendor, Map<String, Object> extraMap) {

        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put(MSG_KEY_MSGID, msgId);
        msgMap.put(MSG_KEY_PRODUCTINSTID, productInstId);
        msgMap.put(MSG_KEY_TARGET, target);
        msgMap.put(MSG_KEY_VENDOR, vendor);

        if(extraMap != null ) msgMap.putAll(extraMap);  //加入额外数据

        String msg = JSON.toJSONString(msgMap);
        logger.debug("Send MQ batchExport message: {}" , msg);
        //发送到消息队列
        mqTemplate.convertAndSend(MQConfig.BATCH_EXPORT_QUEUE_NAME, msg);
    }

    //发送charge消息(进行出账计算)
    public void sendChargeMessage(String msgId, String productInstId, Long chargeId, Map<String, Object> extraMap ) {

        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put(MSG_KEY_MSGID, msgId);
        msgMap.put(MSG_KEY_PRODUCTINSTID, productInstId);
        msgMap.put(MSG_KEY_CHARGE_ID, chargeId);

        if(extraMap != null ) msgMap.putAll(extraMap);  //加入额外数据

        String msg = JSON.toJSONString(msgMap);
        logger.debug("Send MQ batchImport message: {}" , msg);
        //发送到消息队列
        mqTemplate.convertAndSend(MQConfig.CHARGE_CALCULATE_QUEUE_NAME, msg);
    }

    //消息处理异常
    //如果收到消息处理有问题，比如写入数据库失败，请抛出RuntimeException异常，MQ会重试，不过重试几次后会失败，这个要注意
    //应用集群问题
    //如果某个应用起来多个实例，如上面的配置，会导致每条消息每个实例都会收到，如果你不想这么做，请在配置里面加上：
    //spring.cloud.stream.bindings.<channelName>.group=分组名
    //每个应用定义一个唯一的分组名，不好和其他应用重复
    @RabbitListener(queues="batchImportQueue")    //监听器监听指定的Queue
    public void processImport(String msg) {
        logger.debug("Thread:[{}] Receive MQ message:{}" ,Thread.currentThread().getName(), msg);
        mqServiceHandler.batchImportQueueHandler(msg);
    }

    @RabbitListener(queues="batchExportQueue")    //监听器监听指定的Queue
    public void processExport(String msg) {
        logger.debug("Thread:[{}] Receive MQ message:{}" ,Thread.currentThread().getName(), msg);
        mqServiceHandler.batchExportQueueHandler(msg);
    }

    @RabbitListener(queues="chargeCalculateQueue")    //监听器监听指定的Queue
    public void processChargeCalculate(String msg) {
        logger.debug("Thread:[{}] Receive MQ message:{}" ,Thread.currentThread().getName(), msg);
        mqServiceHandler.chargeCalculateQueueHandler(msg);
    }
}

