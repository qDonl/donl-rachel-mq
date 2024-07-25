package top.donl.mq.rocket.consumer;

import top.donl.mq.common.codec.MessageDecoder;
import top.donl.mq.common.consumer.CruxMQConsumer;
import top.donl.mq.common.factory.CruxConsumerFactory;
import top.donl.mq.common.handler.MessageHandler;
import top.donl.mq.common.model.CruxConsumer;
import top.donl.mq.common.props.MQProperties;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Crux
 */


public class RocketMQConsumerFactory extends CruxConsumerFactory {

    public RocketMQConsumerFactory(MQProperties properties, MessageDecoder decoder) {
        super(properties, decoder);
    }

    @Override
    public CruxMQConsumer create(CruxConsumer consumer) {
        String consumerGroup = consumer.getConsumerGroup();

        Map<String, Map<String, MessageHandler<?>>> messageHandlerMap = consumer.toMessageHandlerMap();
        Map<String, Map<String, Type>> paramTypeMap = consumer.toParamTypeMap();
        Map<String, Map<String, MessageDecoder>> decoderMap = consumer.toDecoderMap();

        return null;
    }
}
