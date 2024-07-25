package top.donl.mq.common.utils;

import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * <p></p>
 *
 * @author Crux
 */

@Slf4j
@UtilityClass
public class ReflectUtil {

    public static void setValue(Object obj, String fieldName, String fieldValue) {
        Field field = getField(obj.getClass(), fieldName);
        if (Objects.isNull(field)) {
            log.debug("Class [{}] do not have field [{}], please check.", obj.getClass().getName(), fieldName);
            return;
        }

        field.setAccessible(true);
        try {
            field.set(obj, fieldValue);
        } catch (IllegalAccessException e) {
            log.debug("set class {} field {} error", obj.getClass().getSimpleName(), fieldName, e);
        }
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = null;
        if (Objects.isNull(clazz) || StringUtils.isBlank(fieldName)) {
            return field;
        }

        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            field = getField(clazz.getSuperclass(), fieldName);
        }

        return field;
    }
}
