package top.donl.mq.common.anno;

import top.donl.mq.common.codec.MessageDecoder;

import java.lang.annotation.*;

/**
 * <p>
 *     消费者处理方法
 * </p>
 *
 * @author Crux
 */

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MQSubscribe {

    String topic() default "";


    String[] tag() default {"*"};

    /**
     * 用法跟{@link #tag} 类似，用||分隔, 可从环境变量取值 ${xx.tags}
     * <br/> 同时设置当前tags优先级更高
     */
    String tags() default "";

    /**
     * 消息解析器
     */
    Class<? extends MessageDecoder>[] messageDecoder() default {};
}
