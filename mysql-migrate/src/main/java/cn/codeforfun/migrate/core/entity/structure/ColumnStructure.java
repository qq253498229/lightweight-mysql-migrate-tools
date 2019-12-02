package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangbin
 */
@Getter
@Setter
public class ColumnStructure implements Serializable {
    private static final long serialVersionUID = 6872882688118902246L;

    @DbUtilProperty("TABLE_SCHEMA")
    private String schema;
    @DbUtilProperty("TABLE_NAME")
    private String table;
    @DbUtilProperty("COLUMN_NAME")
    private String name;
    @DbUtilProperty("ORDINAL_POSITION")
    private Long position;
    @DbUtilProperty("COLUMN_DEFAULT")
    private String defaultValue;
    @DbUtilProperty("IS_NULLABLE")
    private String nullable;
    @DbUtilProperty("DATA_TYPE")
    private String type;
    @DbUtilProperty("CHARACTER_MAXIMUM_LENGTH")
    private Long maxLength;
    @DbUtilProperty("NUMERIC_PRECISION")
    private Long numericPrecision;
    @DbUtilProperty("NUMERIC_SCALE")
    private Long numericScale;
    @DbUtilProperty("DATETIME_PRECISION")
    private Long datetimePrecision;
    @DbUtilProperty("CHARACTER_SET_NAME")
    private String character;
    @DbUtilProperty("COLLATION_NAME")
    private String collation;
    @DbUtilProperty("COLUMN_TYPE")
    private String columnType;
    @DbUtilProperty("COLUMN_KEY")
    private String columnKey;
    @DbUtilProperty("EXTRA")
    private String extra;
    @DbUtilProperty("COLUMN_COMMENT")
    private String comment;
    @DbUtilProperty("GENERATION_EXPRESSION")
    private String generationExpression;

}
