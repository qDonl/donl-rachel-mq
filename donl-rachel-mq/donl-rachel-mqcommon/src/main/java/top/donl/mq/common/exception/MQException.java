package top.donl.mq.common.exception;

import top.donl.util.exceptioins.crux.CruxException;

/**
 * <p></p>
 *
 * @author donl.wu
 * @since 2024/07/21 19:28:20
 */


public class MQException extends CruxException {
    private static final long serialVersionUID = -6000114438856441376L;

    public MQException(Integer code, String msg) {
        super(code, msg);
    }
}
