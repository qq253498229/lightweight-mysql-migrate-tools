package cn.codeforfun.migrate.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangbin
 */
public class MigrateBeanTools {
    public static <T> Map<String, String> customColumn(Class<T> clazz) {
        Map<String, String> map = new HashMap<>(0);
        map.put("SCHEMA_NAME", "name");
        map.put("DEFAULT_CHARACTER_SET_NAME", "character");
        map.put("DEFAULT_COLLATION_NAME", "collate");
        //todo
//        Field[] fields = clazz.getDeclaredFields();
//        for (Field field : fields) {
//            if (field.isAnnotationPresent(DbUtilProperty.class)) {
//                System.out.println(1);
//            }
//            System.out.println(1);
//            DbUtilProperty[] annotationsByType = field.getAnnotationsByType(DbUtilProperty.class);
//        }
        return map;
    }
}
