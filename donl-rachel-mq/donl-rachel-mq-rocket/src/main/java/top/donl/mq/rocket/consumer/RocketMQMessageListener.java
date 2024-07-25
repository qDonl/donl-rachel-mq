package top.donl.mq.rocket.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import top.donl.mq.common.codec.MessageDecoder;
import top.donl.mq.common.constant.MQHeaderConst;
import top.donl.mq.common.handler.MessageHandler;
import top.donl.mq.common.listener.AbstractMessageListener;
import top.donl.mq.common.utils.ReflectUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p>
 *     RocketMQ 消费者监听处理器
 * </p>
 *
 * @author Crux
 */

@Slf4j
public class RocketMQMessageListener extends AbstractMessageListener {


    public RocketMQMessageListener(
            Map<String, Map<String, Type>> messageTypeMap,
            Map<String, Map<String, MessageHandler<?>>> messageHandlerMap,
            Map<String, Map<String, MessageDecoder>> decoderMap) {
        super(messageTypeMap, messageHandlerMap, decoderMap);
    }

    @Override
    protected Boolean process(Object obj) {
        MessageExt msg = (MessageExt) obj;
        String msgId = msg.getMsgId();

        try {
            Object body = null;
            String topic = msg.getTopic();
            String tags = StringUtils.isBlank(msg.getTags()) ? "*" : msg.getTags();

            if (MapUtils.isNotEmpty(messageTypeMap) && messageTypeMap.containsKey(topic) && MapUtils.isNotEmpty(messageTypeMap.get(topic))) {
                try {

                    MessageDecoder decoder = getMessageDecoder(topic, tags);
                    Type type = getMessageType(topic, tags);
                    if (type instanceof Class<?>) {
                        body = decoder.decode(msg.getBody(), (Class) type);
                    } else {
                        body = decoder.decode(msg.getBody(), toTypeReference(type));
                    }

                } catch (Exception e) {
                    log.error("Rocket Consumer process err: ", e);
                }

                if (body == null) {
                    log.warn("decode fail msgId: {}", msgId);
                    return true;
                }
            }

            ReflectUtil.setValue(body, MQHeaderConst.MSG_ID_FIELD, msgId);
            invoke(topic, tags, msgId, msg.getProperties(), body);
            return true;
        } catch (Exception e) {
            log.error("rocket handle error: ", e);
        }

        log.warn("consume fail , ask for re-consume , msgId: {}", msgId);
        return false;
    }
}
