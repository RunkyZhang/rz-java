//package com.ww.common.architecture.mq;
//
//import com.wantwant.sfa.common.base.JacksonHelper;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.core.MessageProperties;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//
//import javax.annotation.Resource;
//import java.util.Map;
//import java.util.UUID;
//
//@Slf4j
//public abstract class AbstractRabbitSender {
//    @Resource
//    protected RabbitTemplate rabbitTemplate;
//
//    public void sendMessage(String exchange, String routingKey, String messageId, Object body, Map<String, Object> headers) {
//        MessageProperties messageProperties = new MessageProperties();
//        messageId = StringUtils.isBlank(messageId) ? UUID.randomUUID().toString() : messageId;
//        messageProperties.setMessageId(messageId);
//        if (null != headers) {
//            for (Map.Entry<String, Object> entry : headers.entrySet()) {
//                if (null == entry) {
//                    continue;
//                }
//                messageProperties.setHeader(entry.getKey(), entry.getValue());
//            }
//        }
//
//        Message message = rabbitTemplate.getMessageConverter().toMessage(JacksonHelper.toJson(body, true), messageProperties);
//        rabbitTemplate.send(exchange, routingKey, message);
//    }
//}
