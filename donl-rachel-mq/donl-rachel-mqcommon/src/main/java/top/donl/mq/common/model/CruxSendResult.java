package top.donl.mq.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 消息发送结果
 * </p>
 *
 * @author Crux
 */


@Data
@Accessors(chain = true)
public class CruxSendResult {
    private String topic;
    private String msgId;
    private String txId;
}
