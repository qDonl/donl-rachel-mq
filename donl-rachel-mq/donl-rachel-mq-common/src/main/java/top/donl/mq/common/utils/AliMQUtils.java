package top.donl.mq.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Crux
 */


public class AliMQUtils {
    private static final Map<String, String> ENV_MAPPER = new HashMap<>();

    private static final String ALI_MQ_TOPIC_PREFIX = "ALI_MQ_TOPIC_";
    private static final String ALI_MQ_CONSUMER_PREFIX = "ALI_MQ_CONSUMER_";

    public static String getUniqueTopic(String topic) {
        String topicEnv = ALI_MQ_TOPIC_PREFIX + topic;
        return getEnvOrDefault(topicEnv, topic);
    }

    public static String getUniqueConsumerGroup(String consumerGroup) {
        String topicEnv = ALI_MQ_CONSUMER_PREFIX + consumerGroup;
        return getEnvOrDefault(topicEnv, consumerGroup);
    }

    private static String getEnvOrDefault(String env, String defaultValue) {
        if (!ENV_MAPPER.containsKey(env)) {
            ENV_MAPPER.put(env, System.getenv(env));
        }
        return StringUtils.defaultIfEmpty(ENV_MAPPER.get(env), defaultValue);
    }
}
