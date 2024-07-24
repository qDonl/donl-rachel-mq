package top.donl.mq.common.codec;

import org.apache.commons.lang3.ObjectUtils;
import top.donl.util.json.JacksonUtil;

import java.nio.charset.StandardCharsets;

/**
 * <p></p>
 *
 * @author Crux
 */


public class JacksonMessageEncoder implements MessageEncoder {

    @Override
    public byte[] encode(Object obj) {
        if (ObjectUtils.isEmpty(obj)) {
            return null;
        }

        return JacksonUtil.toJson(obj).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String encode2String(Object obj) {
        if (ObjectUtils.isEmpty(obj)) {
            return null;
        }
        return JacksonUtil.toJson(obj);
    }
}
