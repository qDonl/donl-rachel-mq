package top.donl.mq.rocket.tx;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.aop.framework.AopProxyUtils;
import top.donl.mq.common.codec.MessageDecoder;
import top.donl.mq.common.constant.MQHeaderConst;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *     事务消息监听器处理
 * </p>
 *
 * @author Crux
 */


@Slf4j
public class TransactionListenerDispatcher implements TransactionListener {
    private final Map<String, CruxTransactionListener<?>> map;
    private final MessageDecoder decoder;
    private final Map<Object, TypeReference<Object>> typeMap = new ConcurrentHashMap<>();

    public TransactionListenerDispatcher(Map<String, CruxTransactionListener<?>> listenerMap, MessageDecoder decoder) {
        if (MapUtils.isEmpty(listenerMap)) {
            this.map = new ConcurrentHashMap<>();
        } else {
            this.map = listenerMap;
        }
        this.decoder = decoder;
    }

    @SuppressWarnings("all")
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        log.debug("local transaction execute {}", msg.getTransactionId());
        String listenerKey = msg.getUserProperty(MQHeaderConst.LISTENER_KEY);

        if (listenerKey == null || !map.containsKey(listenerKey)) {
            return CruxTransactionState.ROLLBACK_MESSAGE.rocketMQ();
        }

        CruxTransactionListener bean = this.map.get(listenerKey);
        TypeReference<Object> typeRef = getTypeReference(bean);
        Object obj = this.decoder.decode(msg.getBody(), typeRef);
        return bean.execute(obj, arg).rocketMQ();
    }

    @SuppressWarnings("all")
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        log.debug("local transaction checker {}", msg.getMsgId());
        String listenerKey = msg.getUserProperty(MQHeaderConst.LISTENER_KEY);

        // 没消息的话忽略
        if (listenerKey == null || !map.containsKey(listenerKey)) {
            return CruxTransactionState.ROLLBACK_MESSAGE.rocketMQ();
        }

        CruxTransactionListener bean = this.map.get(listenerKey);
        TypeReference<Object> typeRef = getTypeReference(bean);
        Object obj = this.decoder.decode(msg.getBody(), typeRef);
        return bean.check(obj).rocketMQ();
    }

    private TypeReference<Object> getTypeReference(CruxTransactionListener<?> bean) {
        return typeMap.computeIfAbsent(bean, (b) -> {
            Class<?> clazz = AopProxyUtils.ultimateTargetClass(b);

            // 要从接口去拿确定的类型
            Type actualType = Arrays.stream(clazz.getGenericInterfaces())
                    .filter(ParameterizedType.class::isInstance)
                    .map(ParameterizedType.class::cast)
                    .filter(it -> CruxTransactionListener.class.equals(it.getRawType()))
                    .map(it -> it.getActualTypeArguments()[0])
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(String.format("%s can't find actual arg types!", bean.getClass().getName())))
                    ;
            return new TypeReference<Object>() {
                @Override
                public Type getType() {
                    return actualType;
                }
            };
        });
    }
}
