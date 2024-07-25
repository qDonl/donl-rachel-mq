package top.donl.mq.common.anno;

import org.springframework.stereotype.Component;
import top.donl.mq.common.enums.ConsumeMode;
import top.donl.mq.common.enums.MessageModel;
import top.donl.mq.common.filter.ConsumerFilter;

import java.lang.annotation.*;

/**
 * <p>
 *     消费者类标记注解
 * </p>
 *
 * @author Crux
 */


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Inherited
public @interface MQConsumer {

    /**
     * 消费者标识，默认为 ${terminus.consumerGroup}, 可以使用 ${xx.xx} 通过环境变量取值
     */
    String consumerGroup() default "";


    /**
     * mq 消费线程， 0表示使用对应mq默认的
     */
    int consumeThreadNums() default 0;

    /**
     * mq 消费线程， 可从环境变量取值,如 ${xxx.threadNums},<br/>
     * 同时设置, {@link #consumeThreadNums()} 优先级更高, 受限于类型不一致，所以新增了该配置
     */
    String consumeThreadNumsEnv() default "";


    /**
     * 广播模式消费： BROADCASTING 集群模式消费： CLUSTERING
     *
     * @return 消息模式
     */
    MessageModel messageMode() default MessageModel.CLUSTERING;

    /**
     * 使用线程池并发消费: CONCURRENTLY, 单线程消费: ORDERLY;
     *
     * @return 消费模式
     */
    ConsumeMode consumeMode() default ConsumeMode.CONCURRENTLY;

    /**
     * 消费端的拦截器，默认所有， 如果不需要拦截器，定义为 {@link top.donl.mq.common.filter.NoOpConsumerFilter}
     */
    Class<? extends ConsumerFilter>[] filters() default {};
}
