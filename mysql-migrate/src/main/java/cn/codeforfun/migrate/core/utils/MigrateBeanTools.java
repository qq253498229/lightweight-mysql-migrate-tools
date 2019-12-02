package cn.codeforfun.migrate.core.utils;

import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangbin
 */
public class MigrateBeanTools {
    public static <T> Map<String, String> customColumn(Class<T> clazz) {
        Map<String, String> map = new HashMap<>(0);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DbUtilProperty.class)) {
                DbUtilProperty annotation = field.getAnnotation(DbUtilProperty.class);
                String name = annotation.value();
                if (ObjectUtils.isEmpty(name)) {
                    name = field.getName();
                }
                String value = field.getName();
                map.put(name, value);
            }
        }
        return map;
    }
}
