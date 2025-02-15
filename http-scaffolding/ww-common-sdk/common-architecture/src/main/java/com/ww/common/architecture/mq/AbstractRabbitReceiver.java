//package com.ww.common.architecture.mq;
//
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ThreadLocalRandom;
//
//@Slf4j
//public abstract class AbstractRabbitReceiver {
//    protected void consumeInternal(Message message, Channel channel, String body) {
//        if (null == message) {
//            log.error("unknown error for consume message: [null == message]; body: {}", body);
//            return;
//        }
//        if (null == channel) {
//            log.error("unknown error for consume message: [null == channel]; body: {}", body);
//            return;
//        }
//        if (null == message.getMessageProperties()) {
//            log.error("unknown error for consume message: [null == message.getMessageProperties()]; body: {}", body);
//            return;
//        }
//
//
//        long startPoint = System.currentTimeMillis();
//        boolean shouldLog = ThreadLocalRandom.current().nextInt(1000) < getSampleRate();
//        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        String messageId = message.getMessageProperties().getMessageId();
//        Map<String, Object> headers = message.getMessageProperties().getHeaders();
//
//        if (shouldLog) {
//            log.info("consume-start({}): exchange({}); queue({}); body({})",
//                    messageId,
//                    message.getMessageProperties().getReceivedExchange(),
//                    message.getMessageProperties().getConsumerQueue(),
//                    body);
//        }
//
//        // 业务逻辑处理
//        try {
//            process(messageId, body, headers);
//
//            if (shouldLog) {
//                log.info("consume-end({}): cost({}); exchange({}); queue({})",
//                        messageId,
//                        System.currentTimeMillis() - startPoint,
//                        message.getMessageProperties().getReceivedExchange(),
//                        message.getMessageProperties().getConsumerQueue());
//            }
//
//            try {
//                channel.basicAck(deliveryTag, false);
//            } catch (IOException e) {
//                // ack失败也没问题。会自动ack
//                log.error("failed to ack; messageId({}); deliveryTag({}); body({})", messageId, deliveryTag, body, e);
//            }
//        } catch (Throwable throwable) {
//            // 消费失败处理
//            if (!shouldLog) {
//                // 如果start日志没打印，那么补全打印start日志
//                log.info("consume-start({}): exchange({}); queue({}); body({})",
//                        messageId,
//                        message.getMessageProperties().getReceivedExchange(),
//                        message.getMessageProperties().getConsumerQueue(),
//                        body);
//            }
//            log.error("consume-failed({}): cost({}); exchange({}); queue({}); body({})",
//                    messageId,
//                    System.currentTimeMillis() - startPoint,
//                    message.getMessageProperties().getReceivedExchange(),
//                    message.getMessageProperties().getConsumerQueue(),
//                    body);
//
//
//            // 抛出异常可以重试。retry次数和总开关和间隔在配置（spring.rabbitmq.listener.simple.retry）中设置
//            if (this.needRetry()) {
//                throw throwable;
//            } else {
//                try {
//                    channel.basicAck(deliveryTag, false);
//                } catch (IOException e) {
//                    // ack失败也没问题。会自动ack
//                    log.error("failed to ack when process message error; messageId({}); deliveryTag({}); body({})", messageId, deliveryTag, body, e);
//                }
//            }
//        }
//    }
//
//    protected abstract void process(String messageId, String body, Map<String, Object> headers);
//
//    // 采样率：1000为100%打印日志
//    protected abstract int getSampleRate();
//
//    // 是否需要retry，当处理消息抛异常的时候
//    protected boolean needRetry() {
//        return false;
//    }
//}
//
//
