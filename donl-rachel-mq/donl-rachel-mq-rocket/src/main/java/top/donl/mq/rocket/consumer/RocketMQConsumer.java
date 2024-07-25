package top.donl.mq.rocket.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.protocol.heartbeat.MessageModel;
import top.donl.mq.common.consumer.CruxMQConsumer;
import top.donl.mq.common.listener.AbstractMessageListener;
import top.donl.mq.common.props.MQProperties;

import java.util.Set;

/**
 * <p></p>
 *
 * @author Crux
 */

@Slf4j
public class RocketMQConsumer extends CruxMQConsumer {
    private final DefaultMQPushConsumer defaultConsumer;

    public RocketMQConsumer(MQProperties properties, String consumerGroup, MessageModel messageModel, int consumerThreadNum) {
        if (properties.getAliYunEnable()) {
            // TODO 阿里云配置, 先使用 rocketmq
        }

        defaultConsumer = new DefaultMQPushConsumer(consumerGroup);
        defaultConsumer.setNamesrvAddr(properties.getMqServerAddress());

        if (messageModel != null) {
            defaultConsumer.setMessageModel(messageModel);
        }

        if (properties.getMaxReconsumeTimes() != null) {
            defaultConsumer.setMaxReconsumeTimes(properties.getMaxReconsumeTimes());
        }
        if (consumerThreadNum > 0) {
            defaultConsumer.setConsumeThreadMax(consumerThreadNum);
            defaultConsumer.setConsumeThreadMin(consumerThreadNum);
        }
    }

    @Override
    public void start() {
        try {
            defaultConsumer.start();
        } catch (Exception e) {
            log.warn("RocketMQ consumer start error, consumerGroup={}, error={}", defaultConsumer.getConsumerGroup(), e.getMessage());
        }
    }

    @Override
    public void setMessageListener(AbstractMessageListener listener) {
        defaultConsumer.registerMessageListener((MessageListener) listener);
    }

    @Override
    public void subscribe(String topic, Set<String> tags) {
        try {
            defaultConsumer.subscribe(topic, StringUtils.join(tags, "||"));
        } catch (MQClientException e) {
            log.warn("RocketMQ consumer subscribe error:" + e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        defaultConsumer.shutdown();
    }
}
