package top.donl.mq.rocket.converter;

import org.apache.commons.collections4.MapUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import top.donl.mq.common.codec.MessageEncoder;
import top.donl.mq.common.enums.DelayTimeLevelEnum;
import top.donl.mq.common.model.CruxMessage;
import top.donl.mq.common.model.CruxSendResult;

/**
 * <p></p>
 *
 * @author Crux
 */


public final class MessageConverter {

    public static Message toMessage(CruxMessage message, MessageEncoder encoder) {
        if (message.getMessageEncoder() != null) {
            encoder = message.getMessageEncoder();
        }

        Message msg = new Message();
        msg.setTopic(message.getTopic());
        msg.setTags(message.getTags());
        msg.setBody(encoder.encode(message.getBody()));

        if (MapUtils.isNotEmpty(message.getHeaders())) {
            message.getHeaders().forEach(msg::putUserProperty);
        }

        rocketMqUpdateDelay(msg, message);
        return msg;
    }

    public static CruxSendResult toResult(SendResult sendResult) {
        CruxSendResult result = new CruxSendResult();
        if (sendResult != null) {
            result.setMsgId(sendResult.getMsgId()).setTopic(sendResult.getMessageQueue().getTopic()).setTxId(sendResult.getTransactionId());
        }

        return result;
    }

    private static void rocketMqUpdateDelay(Message msg, CruxMessage message) {
        if (message.getDelay() != 0 && message.getDelayUnit() != null) {
            long mills = message.getDelayUnit().toMillis(message.getDelay());
            DelayTimeLevelEnum timeLevelEnum = null;
            // 按从小到大的顺序取时间级别
            for (DelayTimeLevelEnum value : DelayTimeLevelEnum.values()) {
                timeLevelEnum = value;
                if (mills <= timeLevelEnum.getTimeDelay()) {
                    break;
                }
            }
            assert timeLevelEnum != null;
            msg.setDelayTimeLevel(timeLevelEnum.getLevel());
        } else if (message.getDelayTimeLevel() != null) {
            msg.setDelayTimeLevel(message.getDelayTimeLevel().getLevel());
        }
    }
}
