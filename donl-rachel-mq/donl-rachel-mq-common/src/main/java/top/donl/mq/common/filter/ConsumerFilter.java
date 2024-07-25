package top.donl.mq.common.filter;

import org.springframework.core.Ordered;

import java.util.Map;

/**
 * <p>
 *     消费逻辑前的拦截， 拦截器定义在消费者上 {@link top.donl.mq.common.anno.MQConsumer}
 * </p>
 *
 * @author Crux
 */


public interface ConsumerFilter extends Ordered {

    /**
     * 前置拦截逻辑
     *
     * @param header 请求头
     * @param body   已反序列化的消息体
     */
    default void before(Map<String, String> header, Object body) {

    }

    /**
     * 后置拦截逻辑
     */
    default void after() {

    }


    @Override
    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
