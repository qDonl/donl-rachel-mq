# 1. 事务消息

事务消息需要一下注意点:
1. 必须携带头 `top.donl.mq.common.constant.MQHeaderConst.LISTENER_KEY`, 对应的 value 为 `CruxTransactionListener#listenerKey()` 返回值
2. 需要实现 `top.donl.mq.rocket.tx.CruxTransactionListener`, 此方法用于处理事务以及事务回查, beanName 必须和其 `#listenerKey()` 返回值相同

- 监听器
```java
@Slf4j
@Component(value = DefaultCruxListenerKeyImpl.LISTENER_KEY)
public class DefaultCruxListenerKeyImpl implements CruxTransactionListener<Person> {

    public static final String LISTENER_KEY = "defaultListenerImpl";

    @Override
    public String listenerKey() {
        return LISTENER_KEY;
    }

    @Override
    public CruxTransactionState execute(Person msgBody, Object arg) {
        System.out.println("Tx execute Receive person: " + JacksonUtil.toJson(msgBody));
        return CruxTransactionState.COMMIT_MESSAGE;
    }

    @Override
    public CruxTransactionState check(Person data) {
        System.out.println("Tx check Receive person: " + JacksonUtil.toJson(data));
        return CruxTransactionState.COMMIT_MESSAGE;
    }
}
```

- 生产者
```java
rocketTxProducer.sendInTx("myTopic", "myTag", DefaultCruxListenerKeyImpl.LISTENER_KEY, "data", "arg");
```