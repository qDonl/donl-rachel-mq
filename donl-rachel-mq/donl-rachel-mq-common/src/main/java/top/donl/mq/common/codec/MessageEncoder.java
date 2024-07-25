package top.donl.mq.common.codec;

/**
 * <p></p>
 *
 * @author Crux
 */


public interface MessageEncoder {
    byte[] encode(Object obj);

    String encode2String(Object obj);
}
