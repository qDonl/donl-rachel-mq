package top.donl.mq.common.adapter;

import lombok.NonNull;
import top.donl.mq.common.codec.MessageEncoder;
import top.donl.mq.common.model.CruxMessage;
import top.donl.mq.common.model.CruxSendResult;

import java.util.Collection;


/**
 * <p>
 *     生产者父类包装
 * </p>
 *
 * @author Crux
 */

public abstract class CruxMQProducer {

    private final MessageEncoder encoder;

    public CruxMQProducer(MessageEncoder encoder) {
        this.encoder = encoder;
    }

    // ==================== 同步发送 ====================
    public CruxSendResult send(String topic, Object obj) {
        return send(topic, "", obj);
    }

    public CruxSendResult send(String topic, String tags, Object obj) {
        return send(topic, tags, obj, "");
    }

    public CruxSendResult send(String topic, String tags, Object body, String messageKey) {
        CruxMessage msg = new CruxMessage().setTopic(topic).setTags(tags).setMessageKey(messageKey).setBody(body);
        return doSend(msg);
    }

    public CruxSendResult send(CruxMessage message) {
        return doSend(message);
    }

    public CruxSendResult send(Collection<CruxMessage> messages) {
        return doBatchSend(messages);
    }

    // ==================== 异步发送 ====================
    public void asyncSend(String topic, Object obj, @NonNull CruxSendCallback callback) {
        asyncSend(topic, "", obj, callback);
    }

    public void asyncSend(String topic, String tags, Object obj, @NonNull CruxSendCallback callback) {
        asyncSend(topic, tags, obj, "", callback);
    }

    public void asyncSend(String topic, String tags, Object body, String messageKey, @NonNull CruxSendCallback callback) {
        CruxMessage msg = new CruxMessage().setTopic(topic).setTags(tags).setBody(body).setMessageKey(messageKey);
        doAsyncSend(msg, callback);
    }

    public void asyncSend(CruxMessage message, CruxSendCallback callback) {
        doAsyncSend(message, callback);
    }

    public void asyncSend(Collection<CruxMessage> messages, CruxSendCallback callback) {
        doAsyncBatchSend(messages, callback);
    }

    // ==================== 顺序发送 ====================
    public CruxSendResult sendOrderly(String topic, String shardingKey, Object obj) {
        return sendOrderly(topic, "", shardingKey, obj);
    }

    public CruxSendResult sendOrderly(String topic, String tags, String shardingKey, Object obj) {
        CruxMessage msg = new CruxMessage().setTopic(topic).setTags(tags).setShardingKey(shardingKey).setBody(obj);
        return sendOrderly(msg);
    }

    public CruxSendResult sendOrderly(CruxMessage message) {
        return doSendOrderly(message);
    }


    protected abstract CruxSendResult doSend(CruxMessage message);
    protected abstract CruxSendResult doBatchSend(Collection<CruxMessage> messages);

    protected abstract void doAsyncSend(CruxMessage message, CruxSendCallback callback);
    protected abstract void doAsyncBatchSend(Collection<CruxMessage> messages, CruxSendCallback callback);

    protected abstract CruxSendResult doSendOrderly(CruxMessage message);
    protected abstract void release();
}
