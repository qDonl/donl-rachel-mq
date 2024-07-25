package top.donl.mq.common.factory;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import top.donl.mq.common.codec.MessageDecoder;
import top.donl.mq.common.consumer.CruxMQConsumer;
import top.donl.mq.common.filter.ConsumerFilter;
import top.donl.mq.common.filter.NoOpConsumerFilter;
import top.donl.mq.common.model.CruxConsumer;
import top.donl.mq.common.props.MQProperties;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @author Crux
 */


public abstract class CruxConsumerFactory implements ApplicationContextAware {
    protected final MQProperties properties;
    protected final MessageDecoder decoder;
    protected ApplicationContext context;
    protected Collection<ConsumerFilter> filters;

    public CruxConsumerFactory(MQProperties properties, MessageDecoder decoder) {
        this.properties = properties;
        this.decoder = decoder;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        Map<String, ConsumerFilter> allFilters = applicationContext.getBeansOfType(ConsumerFilter.class);
        this.filters = allFilters.values().stream().sorted(Comparator.comparing(Ordered::getOrder)).collect(Collectors.toList());
    }

    public List<CruxMQConsumer> create(List<CruxConsumer> consumers) {
        return consumers.stream().map(this::create).collect(Collectors.toList());
    }

    protected Collection<ConsumerFilter> getFilters(CruxConsumer consumer) {
        Collection<Class<? extends ConsumerFilter>> filterClass = consumer.getFilters();

        if (CollectionUtils.isEmpty(filterClass)) {
            return this.filters;
        } else if (filterClass.contains(NoOpConsumerFilter.class)) {
            return Collections.emptyList();
        } else {
            return filterClass.stream().map(it -> this.context.getBean(it)).sorted(Comparator.comparing(Ordered::getOrder)).collect(Collectors.toList());
        }
    }

    public abstract CruxMQConsumer create(CruxConsumer consumer);
}
