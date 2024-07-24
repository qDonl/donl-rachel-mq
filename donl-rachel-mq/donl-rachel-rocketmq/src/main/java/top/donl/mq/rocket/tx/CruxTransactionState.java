package top.donl.mq.rocket.tx;

import org.apache.rocketmq.client.producer.LocalTransactionState;

/**
 * <p></p>
 *
 * @author Crux
 */


public enum CruxTransactionState {
    /**
     * 提交消息
     */
    COMMIT_MESSAGE,

    /**
     * 回滚消息
     */
    ROLLBACK_MESSAGE,
    UNKNOWN;

    public LocalTransactionState rocketMQ() {
        switch (this) {
            case COMMIT_MESSAGE:
                return LocalTransactionState.COMMIT_MESSAGE;
            case ROLLBACK_MESSAGE:
                return LocalTransactionState.ROLLBACK_MESSAGE;
            case UNKNOWN:
            default:
                return LocalTransactionState.UNKNOW;
        }
    }
}
