package top.donl.mq.common.adapter;


import top.donl.mq.common.model.CruxSendResult;

/**
 * <p>
 *     异步发送, 回调结果处理
 * </p>
 *
 * @author Crux
 */


public interface CruxSendCallback {
    void onSuccess(final CruxSendResult result);

    void onException(final Throwable e);
}
