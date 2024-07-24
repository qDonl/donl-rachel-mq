package top.donl.mq.rocket;

import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

/**
 * <p>
 *     顺序发送消息,处理器
 * </p>
 *
 * @author Crux
 */


public class RocketMsgSelector implements MessageQueueSelector {

    @Override
    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
        int select = arg == null ? 0 : Math.abs(arg.hashCode());
        if (select < 0) {
            select = 0;
        }
        return mqs.get(select % mqs.size());
    }
}
