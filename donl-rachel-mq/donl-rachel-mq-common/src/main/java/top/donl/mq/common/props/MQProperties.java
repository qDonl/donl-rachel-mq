package top.donl.mq.common.props;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.donl.mq.common.enums.ClientTypeEnum;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * <p></p>
 *
 * @author Crux
 */


@Data
@Component
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "crux")
public class MQProperties implements Serializable {

    private final Environment env;

    private String mqServerAddress;
    private String productGroup;
    private String transactionProductGroup;
    private String consumerGroup;
    private String topic;
    private String defaultConsumerInstanceName;
    private Integer sendMsgTimeout = 5000;
    private AliYunMQProperties aliyun;

    /**
     * 客户端类型
     */
    private ClientTypeEnum clientType;

    /**
     * 最大重试消费次数，设置之后，会将超过重复次数的扔到死信队列，-1代表一直重试
     */
    private Integer maxReconsumeTimes = 3;

    /**
     * mq日志级别
     */
    @Value("${MQ_LOG_LEVEL:ERROR}")
    private String mqLogLevel;

    /**
     * mq日志文件保留个数
     */
    @Value("${MQ_LOG_FILE_MAX_INDEX:1}")
    private String mqLogFileMaxIndex;

    /**
     * mq日志文件大小,单位字节,默认1m
     */
    @Value("${MQ_LOG_FILE_MAX_SIZE:1000000}")
    private String mqLogFileMaxSize;

    @PostConstruct
    public void setEnvProperties() {
        if (ClientTypeEnum.ONS.equals(clientType)) {
            System.setProperty(ONS_LOG_LEVEL, mqLogLevel);
            System.setProperty(ONS_LOG_FILE_MAX_INDEX, mqLogFileMaxIndex);
            System.setProperty(ONS_LOG_FILE_MAX_SIZE, mqLogFileMaxSize);
        } else if (ClientTypeEnum.ROCKETMQ.equals(clientType)) {
            System.setProperty(ROCKETMQ_LOG_LEVEL, mqLogLevel);
            System.setProperty(ROCKETMQ_LOG_FILE_MAX_INDEX, mqLogFileMaxIndex);
            System.setProperty(ROCKETMQ_LOG_FILE_MAX_SIZE, mqLogFileMaxSize);
        }
    }

    public Boolean getAliYunEnable() {
        return ClientTypeEnum.ONS.equals(clientType) && aliyun != null && StringUtils.isNotBlank(aliyun.getAccessKey())
                && StringUtils.isNotBlank(aliyun.getSecretKey());
    }

    private static final String ROCKETMQ_LOG_LEVEL = "rocketmq.client.logLevel";
    private static final String ROCKETMQ_LOG_FILE_MAX_INDEX = "rocketmq.client.logFileMaxIndex";
    private static final String ROCKETMQ_LOG_FILE_MAX_SIZE = "rocketmq.client.logFileMaxSize";

    private static final String ONS_LOG_LEVEL = "ons.client.logLevel";
    private static final String ONS_LOG_FILE_MAX_INDEX = "ons.client.logFileMaxIndex";
    private static final String ONS_LOG_FILE_MAX_SIZE = "ons.client.logFileMaxSize";
}
