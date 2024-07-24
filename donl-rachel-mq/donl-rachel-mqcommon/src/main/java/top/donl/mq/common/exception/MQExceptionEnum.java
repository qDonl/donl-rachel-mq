package top.donl.mq.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.donl.util.exceptioins.crux.CruxBaseAssert;
import top.donl.util.exceptioins.crux.CruxException;

/**
 * 自定义异常枚举信息
 *
 * @author Crux
 * @since 2022-10-16 08:10:19
 */

@Getter
@AllArgsConstructor
public enum MQExceptionEnum implements CruxBaseAssert {

    PRODUCER_START_ERROR(240721_001, "producer start error"),
    MSG_SEND_ERROR(240721_002, "msg send error"),
    MSG_CANNOT_EMPTY(240721_003, "msg can not empty"),
    NOT_SUPPORT_OPTIONS(240721_004, "not support options"),
    ;

    private final Integer code;
    private final String msg;

    @Override
    public CruxException getExp(Integer code, String msg) {
        return new CruxException(code, msg);
    }
}
