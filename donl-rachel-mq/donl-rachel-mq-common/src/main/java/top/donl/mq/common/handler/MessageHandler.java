package top.donl.mq.common.handler;

/**
 * <p>
 *     消费者消息业务处理逻辑
 * </p>
 *
 * @author Crux
 */


public interface MessageHandler<T> {
    /**
     * 消费者消息处理逻辑
     */
    default void process(T data) {
    }

    /**
     * 消息处理
     *
     * @param data  数据内容
     * @param msgId 消息ID
     */
    default void process(T data, String msgId) {
        this.process(data);
    }

    default void process(T data, String msgId, String topic, String tag) {
        this.process(data, msgId);
    }

    @SuppressWarnings("unchecked")
    default void adaptProcess(Object body, String msgId, String topic, String tag) {
        this.process((T) body, msgId, topic, tag);
    }
}
