package cn.codeforfun.migrate.core.entity.structure.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记DbUtil字段
 * 方便注入值
 *
 * @author wangbin
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DbUtilProperty {
    /**
     * 标记字段对应的SQL字段名
     *
     * @return SQL字段名
     */
    String value() default "";
}
