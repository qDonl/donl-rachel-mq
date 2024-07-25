package top.donl.mq.common.model;

import lombok.Data;
import lombok.experimental.Accessors;
import top.donl.mq.common.codec.MessageEncoder;
import top.donl.mq.common.enums.DelayTimeLevelEnum;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 *
 * @author Crux
 */


@Data
@Accessors(chain = true)
public class CruxMessage implements Serializable {
    private static final long serialVersionUID = -6053923976027770337L;

    /**
     * topic 必填
     */
    private String topic;

    /**
     * tag 必填
     */
    private String tags;

    /**
     * 消息头
     */
    private Map<String, String> headers;

    /**
     * body 必填
     */
    private Object body;

    /**
     * 顺序消息的分片key，可填， rocketmq和ons使用
     */
    private Object shardingKey;

    /**
     * 该条消息的消息id，特定场景下会存在，比如事务消息的回查
     */
    private String msgId;

    /**
     * 消息key，可填，在业务查询消息的时候使用
     */
    private String messageKey = "";


    /**
     * 延迟时间
     */
    private long delay = 0L;

    /**
     * 对应延迟单位
     */
    private TimeUnit delayUnit;

    /**
     * 消息级别自定义的编码器，不存在则用全局的编码器
     */
    private MessageEncoder messageEncoder;

    /**
     * 消息延时级别
     */
    private DelayTimeLevelEnum delayTimeLevel;
}
