package top.donl.mq.common.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import top.donl.mq.common.codec.MessageDecoder;
import top.donl.mq.common.exception.MQExceptionEnum;
import top.donl.mq.common.filter.ConsumerFilter;
import top.donl.mq.common.handler.MessageHandler;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Crux
 */


@Slf4j
public abstract class AbstractMessageListener {
    // <topic, <tag, argType>>
    protected final Map<String, Map<String, Type>> messageTypeMap;

    // <topic, <tag, MessageHandler>>
    protected final Map<String, Map<String, MessageHandler<?>>> messageHandlerMap;

    // <topic, <tag, MessageDecoder>>
    protected final Map<String, Map<String, MessageDecoder>> decoderMap;

    protected MessageDecoder defaultDecoder;

    @Setter
    private List<ConsumerFilter> filters = Collections.emptyList();

    public AbstractMessageListener(
            Map<String, Map<String, Type>> messageTypeMap,
            Map<String, Map<String, MessageHandler<?>>> messageHandlerMap,
            Map<String, Map<String, MessageDecoder>> decoderMap) {
        this.messageTypeMap = messageTypeMap;
        this.messageHandlerMap = messageHandlerMap;
        this.decoderMap = decoderMap;
    }

    protected void invoke(String topic, String tag, String msgId, Map<String, String> header, Object body) {
        executeBeforeFilter(header, body);
        try {
            getMessageHandler(topic, tag).adaptProcess(body, msgId, topic, tag);
        } finally {
            executeAfterFilter();
        }
    }

    private void executeBeforeFilter(Map<String, String> header, Object body) {
        for (ConsumerFilter filter : filters) {
            filter.before(header, body);
        }
    }

    private void executeAfterFilter() {
        for (int i = filters.size() - 1; i >= 0; i--) {
            filters.get(i).after();
        }
    }

    /**
     * 获取业务消息处理器
     */
    protected MessageHandler<?> getMessageHandler(String topic, String tag) {
        Map<String, MessageHandler<?>> topicMap = messageHandlerMap.get(topic);
        MessageHandler<?> messageHandler = topicMap.get(topicMap.containsKey(tag) ? tag : "*");
        if (messageHandler == null) {
            MQExceptionEnum.CANNOT_FIND_MESSAGE_HANDLER.errorMsg(String.format("not messageHandler can handle topic=%s, tag=%s", topic, tag));
        }

        return messageHandler;
    }

    /**
     * 获取解码器
     */
    protected MessageDecoder getMessageDecoder(String topic, String tag) {
        MessageDecoder decoder = decoderMap.get(topic).get(decoderMap.get(topic).containsKey(tag) ? tag : "*");
        if (decoder == null) {
            return defaultDecoder;
        }
        return decoder;
    }

    /**
     * 获取消息入参类型
     */
    protected Type getMessageType(String topic, String tag) {
        return messageTypeMap.get(topic).get(messageTypeMap.get(topic).containsKey(tag) ? tag : "*");
    }

    protected TypeReference<Object> toTypeReference(Type type) {
        return new TypeReference<Object>() {
            @Override
            public Type getType() {
                return type;
            }
        };
    }

    protected abstract Boolean process(Object obj);
}
