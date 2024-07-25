package top.donl.mq.common.model;

import lombok.Data;
import top.donl.mq.common.codec.MessageDecoder;
import top.donl.mq.common.handler.MessageHandler;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

/**
 * <p></p>
 *
 * @author Crux
 */

@Data
public class Subscription {
    private String topic;
    private Set<String> tags;
    private MessageHandler<?> messageHandler;

    /**
     * 消费者业务处理 bean 对象
     */
    private Object bean;

    /**
     * 消费者业务处理方法名
     */
    private Method method;

    /**
     * 参数类型
     */
    private Type parameterClass;
    private MessageDecoder messageDecoder;

    public Type getParameterClass() {
        if (parameterClass == null) {
            // 泛型化参数
            if (messageHandler != null) {
                parameterClass = Arrays.stream(messageHandler.getClass().getGenericInterfaces())
                        .filter(ParameterizedType.class::isInstance)
                        .map(ParameterizedType.class::cast)
                        // 是 messageHandler 类型的
                        .filter(it -> MessageHandler.class.equals(it.getRawType()))
                        // 取第一个泛型
                        .flatMap((ParameterizedType parameterizedType) -> Stream.of(parameterizedType.getActualTypeArguments()))
                        .findFirst().orElseThrow(NullPointerException::new);
            }

            // 方法参数必定只有一个
            if (method != null) {
                parameterClass = method.getGenericParameterTypes()[0];
            }
        }
        return parameterClass;
    }

    /**
     * 响应当前定义关系的唯一标识
     */
    public String identity() {
        String tag;
        if (tags == null || tags.isEmpty()) {
            tag = "*";
        } else {
            tag = String.join("||", tags);
        }
        return String.format("%s:%s", topic, tag);
    }
}
