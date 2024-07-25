package top.donl.mq.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *     rocket 默认延时时间
 * </p>
 *
 * @author Crux
 */


@Getter
@AllArgsConstructor
public enum DelayTimeLevelEnum {
    LEVEL_ZERO(0, 0L, "0s"),
    LEVEL_ONE(1, 1000L, "1s"),
    LEVEL_TWO(2, 5000L, "5s"),
    LEVEL_THREE(3, 10000L, "10s"),
    LEVEL_FOUR(4, 30000L, "30s"),
    LEVEL_FIVE(5, 60000L, "1m"),
    LEVEL_SIX(6, 120000L, "2m"),
    LEVEL_SEVEN(7, 180000L, "3m"),
    LEVEL_EIGHT(8, 240000L, "4m"),
    LEVEL_NINE(9, 300000L, "5m"),
    LEVEL_TEN(10, 360000L, "6m"),
    LEVEL_ELEVEN(11, 420000L, "7m"),
    LEVEL_TWELVE(12, 480000L, "8m"),
    LEVEL_THIRTEEN(13, 540000L, "9m"),
    LEVEL_FOURTEEN(14, 600000L, "10m"),
    LEVEL_FIFTEEN(15, 1200000L, "20m"),
    LEVEL_SIXTEEN(16, 1800000L, "30m"),
    LEVEL_SEVENTEEN(17, 3600000L, "1h"),
    LEVEL_EIGHTEEN(18, 7200000L, "2h");

    private final Integer level;
    private final Long timeDelay;
    private final String desc;
}
