package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * @author wangbin
 */
@Getter
@Setter
@EqualsAndHashCode
public class Column implements Difference, Serializable {
    private static final long serialVersionUID = -5078043863310019475L;

    public static final String FLAG_NOT_NULL = "NO";
    public static final String FLAG_DEFAULT_NULL = "YES";
    public static final String FLAG_AUTO_INCREMENT = "auto_increment";

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

    public String getDeleteSql() {
        return "ALTER TABLE `" + this.table + "` DROP COLUMN `" + this.name + "`;\n";
    }

    public String getCreateSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("`").append(this.name).append("`").append(" ");
        sb.append(this.columnType).append(" ");
        if (!ObjectUtils.isEmpty(this.collation)) {
            sb.append("COLLATE ").append(this.collation).append(" ");
        }
        if (FLAG_NOT_NULL.equals(this.nullable)) {
            sb.append("NOT NULL ");
        } else if (FLAG_DEFAULT_NULL.equals(this.nullable)) {
            sb.append("DEFAULT NULL ");
        }
        if (!ObjectUtils.isEmpty(this.defaultValue)) {
            sb.append("DEFAULT ").append(this.defaultValue).append(" ");
        }
        if (FLAG_AUTO_INCREMENT.equals(this.extra)) {
            sb.append("AUTO_INCREMENT ");
        }
        if (!ObjectUtils.isEmpty(this.comment)) {
            sb.append("COMMENT '").append(this.comment).append("' ");
        }
        sb.append(",");
        return sb.toString();
    }

    public String getUpdateSql() {
        String createSql = "ALTER TABLE `" + this.table + "` MODIFY ";
        createSql += getCreateSql();
        createSql = createSql.substring(0, createSql.length() - 1) + ";";
        return createSql;
    }

}
