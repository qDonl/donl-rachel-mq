package top.donl.mq.rocket;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.donl.mq.common.adapter.CruxMQProducer;
import top.donl.mq.common.codec.JacksonMessageDecoder;
import top.donl.mq.common.codec.JacksonMessageEncoder;
import top.donl.mq.common.codec.MessageDecoder;
import top.donl.mq.common.codec.MessageEncoder;
import top.donl.mq.rocket.tx.CruxTransactionListener;
import top.donl.mq.rocket.tx.RocketTxProducer;
import top.donl.mq.rocket.tx.TransactionListenerDispatcher;

import java.util.Map;


/**
 * <p>
 * 创建默认生产者
 * </p>
 *
 * @author Crux
 */

@Slf4j
@Configuration
@EnableConfigurationProperties(value = {RocketMQProperties.class})
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CruxMQAutoConfig {

    @Bean(value = "rocketProducer", destroyMethod = "release")
    public CruxMQProducer rocketProducer(RocketMQProperties properties, MessageEncoder encoder) throws MQClientException {
        return new RocketProducer(properties, encoder);
    }

    @Bean(value = "rocketTxProducer", destroyMethod = "release")
    public RocketTxProducer rocketTxProducer(RocketMQProperties properties, TransactionListenerDispatcher dispatcher, MessageEncoder encoder) throws MQClientException {
        return new RocketTxProducer(properties, dispatcher, encoder);
    }

    @Bean
    public TransactionListenerDispatcher transactionListenerDispatcher(Map<String, CruxTransactionListener<?>> listenerMap, MessageDecoder decoder) {
        return new TransactionListenerDispatcher(listenerMap, decoder);
    }

    @Bean
    @ConditionalOnMissingBean(MessageEncoder.class)
    public MessageEncoder messageEncoder() {
        return new JacksonMessageEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(MessageDecoder.class)
    public MessageDecoder messageDecoder() {
        return new JacksonMessageDecoder();
    }
}
