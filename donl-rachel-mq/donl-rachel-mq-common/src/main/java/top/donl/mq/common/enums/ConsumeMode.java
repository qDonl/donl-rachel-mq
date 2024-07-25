package top.donl.mq.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p></p>
 *
 * @author Crux
 */

@Getter
@AllArgsConstructor
public enum ConsumeMode {
    CONCURRENTLY,
    ORDERLY
}
