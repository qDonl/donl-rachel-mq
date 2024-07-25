package top.donl.mq.common.model;

import lombok.Data;
import top.donl.mq.common.codec.MessageDecoder;
import top.donl.mq.common.enums.ConsumeMode;
import top.donl.mq.common.enums.MessageModel;
import top.donl.mq.common.filter.ConsumerFilter;
import top.donl.mq.common.handler.MessageHandler;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

/**
 * <p>
 * {@link top.donl.mq.common.anno.MQConsumer} 和 {@link top.donl.mq.common.anno.MQSubscribe} 属性配置
 * </p>
 *
 * @author Crux
 */


@Data
public class CruxConsumer {
    private String consumerGroup;
    private List<Subscription> subscriptions;

    /**
     * mq 消费者线程
     */
    private int consumerThreadNums = 0;

    private MessageModel messageModel = MessageModel.CLUSTERING;

    private ConsumeMode consumeMode = ConsumeMode.CONCURRENTLY;

    private Collection<Class<? extends ConsumerFilter>> filters = Collections.emptyList();


    /**
     * 响应 topic-tag-messageHandler 格式的map
     *
     * @return Map<topic, < tag, messageHandler>>
     */
    public Map<String, Map<String, MessageHandler<?>>> toMessageHandlerMap() {
        return toMap(Subscription::getMessageHandler);
    }

    /**
     * 响应 topic-tag-decoder 格式的map
     *
     * @return Map<topic, < tag, decoder>>
     */
    public Map<String, Map<String, MessageDecoder>> toDecoderMap() {
        return toMap(Subscription::getMessageDecoder);
    }

    /**
     * 响应 topic-tag-paramType 格式的map
     *
     * @return Map<topic, < tag, paramType>>
     */
    public Map<String, Map<String, Type>> toParamTypeMap() {
        return toMap(Subscription::getParameterClass);
    }


    private <T> Map<String, Map<String, T>> toMap(Function<Subscription, T> func) {
        if (subscriptions == null || subscriptions.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Map<String, T>> result = new HashMap<>(subscriptions.size());
        for (Subscription s : subscriptions) {
            result.putIfAbsent(s.getTopic(), new HashMap<>(5));
            for (String tag : s.getTags()) {
                result.get(s.getTopic()).put(tag, func.apply(s));
            }
        }
        return result;
    }

    /**
     * 生成订阅关系， 避免订阅关系不一致
     * https://help.aliyun.com/document_detail/43523.html?spm=a2c4g.11186623.6.582.LIRX51#title-f4d-kmk-wfu
     */
    public Map<String, Set<String>> toSubscription() {
        if (subscriptions == null || subscriptions.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Set<String>> result = new HashMap<>(5);
        for (Subscription s : subscriptions) {
            result.putIfAbsent(s.getTopic(), new TreeSet<>());
            Set<String> tags = result.get(s.getTopic());
            if (s.getTags().contains("*")) {
                tags.clear();
                tags.add("*");
            } else {
                tags.addAll(s.getTags());
            }
        }
        return result;
    }

}
