package com.highplace.biz.pm.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    //用于批量导入档案的队列
    public static String BATCH_IMPORT_QUEUE_NAME = "batchImportQueue";

    //用于批量导出档案的队列
    public static String BATCH_EXPORT_QUEUE_NAME = "batchExportQueue";

    //用于费用出账计算的队列
    public static String CHARGE_CALCULATE_QUEUE_NAME = "chargeCalculateQueue";

    @Bean
    public Queue batchImportQueue() {
        return new Queue(BATCH_IMPORT_QUEUE_NAME);
    }

    @Bean
    public Queue batchExportQueue() {
        return new Queue(BATCH_EXPORT_QUEUE_NAME);
    }

    @Bean
    public Queue chargeCalculateQueue() {
        return new Queue(CHARGE_CALCULATE_QUEUE_NAME);
    }
}
