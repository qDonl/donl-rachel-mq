package top.donl.mq.common.codec;

import com.fasterxml.jackson.core.type.TypeReference;
import top.donl.util.json.JacksonUtil;

import java.nio.charset.StandardCharsets;

/**
 * <p></p>
 *
 * @author Crux
 */


public class JacksonMessageDecoder implements MessageDecoder{

    @Override
    public <T> T decode(byte[] bytes, Class<T> clazz) {
        String data = new String(bytes);
        return decode(data, clazz);
    }

    @Override
    public <T> T decode(String data, Class<T> clazz) {
        return JacksonUtil.fromJson(data, clazz);
    }

    @Override
    public <T> T decode(byte[] bytes, TypeReference<T> typeRef) {
        String data = new String(bytes, StandardCharsets.UTF_8);
        return decode(data, typeRef);
    }

    @Override
    public <T> T decode(String data, TypeReference<T> typeRef) {
        return JacksonUtil.fromJson(data, typeRef);
    }
}
