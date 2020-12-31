package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * 字段结构定义
 *
 * @author wangbin
 */
@Getter
@Setter
public class Column implements Difference, Serializable {
    private static final long serialVersionUID = -5078043863310019475L;

    public static final String FLAG_NOT_NULL = "NO";
    public static final String FLAG_DEFAULT_NULL = "YES";
    public static final String FLAG_AUTO_INCREMENT = "auto_increment";

    @DbUtilProperty("TABLE_SCHEMA")
    private String schema;
    @DbUtilProperty("TABLE_NAME")
    private String tableName;
    @DbUtilProperty("COLUMN_NAME")
    private String name;
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

    private Table table;

    @JsonIgnore
    @Override
    public String getDeleteSql() {
        return "ALTER TABLE `" + this.tableName + "` DROP COLUMN `" + this.name + "`;";
    }

    /**
     * 创建表的时候用到的sql
     *
     * @return sql
     */
    @JsonIgnore
    public String getCreateTableSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("`").append(this.name).append("`").append(" ");
        sb.append(this.columnType).append(" ");
        if (!ObjectUtils.isEmpty(this.collation)) {
            sb.append("COLLATE ").append(this.collation).append(" ");
        }
        if (FLAG_NOT_NULL.equals(this.nullable)) {
            sb.append("NOT NULL ");
        }
        if (!ObjectUtils.isEmpty(this.defaultValue)) {
            sb.append("DEFAULT ").append(this.defaultValue).append(" ");
        } else if (FLAG_DEFAULT_NULL.equals(this.nullable)) {
            sb.append("DEFAULT NULL ");
        }
        sb.append(this.extra).append(" ");
        if (!ObjectUtils.isEmpty(this.comment)) {
            sb.append("COMMENT '").append(this.comment).append("' ");
        }
        sb.append(",");
        return sb.toString();
    }

    @JsonIgnore
    @Override
    public String getCreateSql() {
        String createSql = "ALTER TABLE `" + this.tableName + "` ADD ";
        createSql += getCreateTableSql();
        createSql = createSql.substring(0, createSql.length() - 1) + ";";
        return createSql;
    }

    @JsonIgnore
    @Override
    public String getUpdateSql() {
        String createSql = "ALTER TABLE `" + this.tableName + "` MODIFY ";
        createSql += getCreateTableSql();
        createSql = createSql.substring(0, createSql.length() - 1) + ";";
        return createSql;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Column)) {
            return false;
        }
        Column column = (Column) o;
        return Objects.equals(getTableName(), column.getTableName()) &&
                Objects.equals(getName(), column.getName()) &&
                ((ObjectUtils.isEmpty(getDefaultValue()) && ObjectUtils.isEmpty(column.getDefaultValue()))
                        || Objects.equals(getDefaultValue(), column.getDefaultValue())) &&
                Objects.equals(getNullable(), column.getNullable()) &&
                Objects.equals(getType(), column.getType()) &&
                Objects.equals(getMaxLength(), column.getMaxLength()) &&
                Objects.equals(getNumericPrecision(), column.getNumericPrecision()) &&
                Objects.equals(getNumericScale(), column.getNumericScale()) &&
                Objects.equals(getDatetimePrecision(), column.getDatetimePrecision()) &&
                Objects.equals(getCharacter(), column.getCharacter()) &&
                Objects.equals(getCollation(), column.getCollation()) &&
                Objects.equals(getExtra(), column.getExtra()) &&
                Objects.equals(getComment(), column.getComment()) &&
                Objects.equals(getGenerationExpression(), column.getGenerationExpression());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTableName(), getName(), getDefaultValue(), getNullable(), getType(), getMaxLength(), getNumericPrecision(), getNumericScale(), getDatetimePrecision(), getCharacter(), getCollation(), getColumnType(), getExtra(), getComment(), getGenerationExpression());
    }

}
