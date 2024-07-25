package top.donl.mq.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *     消息模式
 * </p>
 *
 * @author Crux
 */


@Getter
@AllArgsConstructor
public enum MessageModel {
    BROADCASTING("BROADCASTING"),
    CLUSTERING("CLUSTERING");

    private final String modeCN;
}
