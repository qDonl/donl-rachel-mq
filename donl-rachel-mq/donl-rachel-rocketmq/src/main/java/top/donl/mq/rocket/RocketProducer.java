package top.donl.mq.rocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import top.donl.mq.common.adapter.CruxMQProducer;
import top.donl.mq.common.adapter.CruxSendCallback;
import top.donl.mq.common.codec.MessageEncoder;
import top.donl.mq.common.exception.MQExceptionEnum;
import top.donl.mq.common.model.CruxMessage;
import top.donl.mq.common.model.CruxSendResult;
import top.donl.mq.rocket.converter.MessageConverter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author Crux
 */

@Slf4j
public class RocketProducer extends CruxMQProducer {

    private final DefaultMQProducer producer;
    private final MessageEncoder encoder;

    public RocketProducer(RocketMQProperties properties, MessageEncoder encoder) throws MQClientException {
        super(encoder);
        this.encoder = encoder;
        this.producer = new DefaultMQProducer(properties.getProducer().getGroup(), true, "");
        producer.setNamesrvAddr(properties.getNameServer());
        producer.setSendMsgTimeout(properties.getProducer().getSendMessageTimeout());
        producer.start();
    }

    @Override
    protected CruxSendResult doSend(CruxMessage message) {
        try {
            return MessageConverter.toResult(producer.send(MessageConverter.toMessage(message, encoder)));
        } catch (Exception e) {
            log.error("rocket send msg error: ", e);
            MQExceptionEnum.MSG_SEND_ERROR.errorMsg();
            return null;
        }
    }

    @Override
    protected CruxSendResult doBatchSend(Collection<CruxMessage> messages) {
        try {
            return MessageConverter.toResult(producer.send(convertMessage(messages)));
        } catch (Exception e) {
            log.error("rocket batch send msg error: ", e);
            MQExceptionEnum.MSG_SEND_ERROR.errorMsg();
            return null;
        }
    }

    @Override
    protected void doAsyncSend(CruxMessage message, CruxSendCallback callback) {
        try {
            producer.send(MessageConverter.toMessage(message, encoder), buildSendCallback(callback));
        } catch (Exception e) {
            log.error("rocket async send msg error: ", e);
            MQExceptionEnum.MSG_SEND_ERROR.errorMsg();
        }
    }

    @Override
    protected void doAsyncBatchSend(Collection<CruxMessage> messages, CruxSendCallback callback) {
        try {
            producer.send(convertMessage(messages), buildSendCallback(callback));
        } catch (Exception e) {
            log.error("rocket async batch send msg error: ", e);
            MQExceptionEnum.MSG_SEND_ERROR.errorMsg();
        }
    }

    @Override
    protected CruxSendResult doSendOrderly(CruxMessage message) {
        try {
            return MessageConverter.toResult(producer.send(MessageConverter.toMessage(message, encoder), new RocketMsgSelector(), message.getShardingKey()));
        } catch (Exception e) {
            log.error("rocket send msg error: ", e);
            MQExceptionEnum.MSG_SEND_ERROR.errorMsg();
            return null;
        }
    }

    @Override
    protected void release() {
        if (this.producer != null) {
            this.producer.shutdown();
        }
    }

    private SendCallback buildSendCallback(CruxSendCallback callback) {
        return new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                callback.onSuccess(MessageConverter.toResult(sendResult));
            }

            @Override
            public void onException(Throwable e) {
                callback.onException(e);
            }
        };
    }

    private List<Message> convertMessage(Collection<CruxMessage> messages) {
        MQExceptionEnum.MSG_CANNOT_EMPTY.assertIsTrue(CollectionUtils.isNotEmpty(messages));
        return messages.stream().map(v -> MessageConverter.toMessage(v, encoder)).collect(Collectors.toList());
    }
}
