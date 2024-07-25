package top.donl.mq.rocket.tx;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import top.donl.mq.common.codec.MessageEncoder;
import top.donl.mq.common.constant.MQHeaderConst;
import top.donl.mq.common.exception.MQExceptionEnum;
import top.donl.mq.common.model.CruxMessage;
import top.donl.mq.common.model.CruxSendResult;
import top.donl.mq.rocket.converter.MessageConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p></p>
 *
 * @author Crux
 */

@Slf4j
public class RocketTxProducer {

    private final TransactionMQProducer producer;
    private final MessageEncoder encoder;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public RocketTxProducer(RocketMQProperties properties, TransactionListenerDispatcher dispatcher, MessageEncoder encoder) throws MQClientException {
        this.encoder = encoder;
        this.producer = new TransactionMQProducer(properties.getProducer().getGroup());

        producer.setNamesrvAddr(properties.getNameServer());
        producer.setSendMsgTimeout(properties.getProducer().getSendMessageTimeout());

        // 参数后边提供出去作为可配选项
        ExecutorService executorService = new ThreadPoolExecutor(5, 20, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("transaction-msg-checker-" + threadNumber.getAndIncrement());
                    return thread;
                });
        producer.setExecutorService(executorService);
        producer.setTransactionListener(dispatcher);
        producer.start();
    }

    public CruxSendResult sendInTx(String topic, String listenerKey, Object data, Object arg) {
        return sendInTx(topic, "", listenerKey, data, arg);
    }

    public CruxSendResult sendInTx(String topic, String tag, String listenerKey, Object data, Object arg) {
        Map<String, String> header = new HashMap<String, String>() {{
            put(MQHeaderConst.LISTENER_KEY, listenerKey);
        }};
        CruxMessage msg = new CruxMessage().setTopic(topic).setTags(tag).setBody(data).setHeaders(header);
        return sendInTx(msg, arg);
    }

    public CruxSendResult sendInTx(CruxMessage message, Object arg) {
        return doSendInTx(message, arg);
    }

    public void release() {
        if (producer != null) {
            producer.shutdown();
        }
    }

    private CruxSendResult doSendInTx(CruxMessage message, Object arg) {
        Message msg = MessageConverter.toMessage(message, encoder);
        try {
            return MessageConverter.toResult(producer.sendMessageInTransaction(msg, arg));
        } catch (Exception e) {
            log.error("sendInTx error", e);
            MQExceptionEnum.MSG_SEND_ERROR.errorMsg();
            return null;
        }
    }
}
