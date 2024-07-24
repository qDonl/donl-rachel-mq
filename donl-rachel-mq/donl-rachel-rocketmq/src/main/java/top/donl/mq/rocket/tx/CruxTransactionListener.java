package top.donl.mq.rocket.tx;

/**
 * <p></p>
 *
 * @author Crux
 */


public interface CruxTransactionListener<T> {

    /**
     * 该本地事务处理组件的唯一key，必须确保全局唯一，在事务消息回查的时候根据这个来定位到该 listenerKey
     */
    String listenerKey();


    /**
     * 执行本地事务，半消息的后置处理
     *
     * @param msgBody 消息体对应的参数
     * @param arg     业务额外参数
     */
    CruxTransactionState execute(T msgBody, Object arg);


    /**
     * 回查本地事务
     */
    CruxTransactionState check(T data);

}
