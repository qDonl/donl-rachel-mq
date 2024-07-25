package top.donl.mq.common.consumer;

import top.donl.mq.common.listener.AbstractMessageListener;

import java.util.Set;

/**
 * <p>消费者处理逻辑</p>
 *
 * @author Crux
 */


public abstract class CruxMQConsumer {
    public abstract void start();

    public abstract void setMessageListener(AbstractMessageListener listener);

    public abstract void subscribe(String topic, Set<String> tags);

    public void stop() {
    }
}
