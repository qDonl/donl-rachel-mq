package top.donl.mq.common.codec;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * <p></p>
 *
 * @author Crux
 */


public interface MessageDecoder {

    <T> T decode(byte[] bytes, Class<T> clazz);

    <T> T decode(String data, Class<T> clazz);

    <T> T decode(byte[] bytes, TypeReference<T> typeRef);

    <T> T decode(String data, TypeReference<T> typeRef);
}
