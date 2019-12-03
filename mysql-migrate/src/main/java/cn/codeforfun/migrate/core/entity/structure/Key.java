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
public class Key implements Difference, Serializable {
    private static final long serialVersionUID = 5489127924529636485L;

    public static final String FLAG_PRIMARY = "PRIMARY";

    @DbUtilProperty("CONSTRAINT_SCHEMA")
    private String schema;
    @DbUtilProperty("CONSTRAINT_NAME")
    private String name;
    @DbUtilProperty("TABLE_NAME")
    private String tableName;
    @DbUtilProperty("COLUMN_NAME")
    private String columnName;
    @DbUtilProperty("ORDINAL_POSITION")
    private Long ordinalPosition;
    @DbUtilProperty("POSITION_IN_UNIQUE_CONSTRAINT")
    private Long positionInUniqueConstraint;
    @DbUtilProperty("REFERENCED_TABLE_SCHEMA")
    private String referencedSchema;
    @DbUtilProperty("REFERENCED_TABLE_NAME")
    private String referencedTable;
    @DbUtilProperty("REFERENCED_COLUMN_NAME")
    private String referencedColumn;

    public String getCreateSql() {
        //获取key sql
        StringBuilder sb = new StringBuilder();
        if (FLAG_PRIMARY.equals(this.name)) {
            // 主键
            sb.append("PRIMARY KEY (`").append(this.columnName).append("`),");
        } else if (ObjectUtils.isEmpty(this.referencedSchema)
                && ObjectUtils.isEmpty(this.referencedTable)
                && ObjectUtils.isEmpty(this.referencedColumn)) {
            // 唯一索引
            sb.append("UNIQUE KEY `").append(this.name).append("` (`").append(this.columnName).append("`),");
        } else {
            // 外键
            sb.append("KEY `").append(this.name).append("` (`").append(this.columnName).append("`),");
            sb.append("CONSTRAINT `").append(this.name).append("` ")
                    .append("FOREIGN KEY (`").append(this.columnName).append("`) ")
                    .append("REFERENCES `").append(this.referencedTable).append("` ")
                    .append("(`").append(this.referencedColumn).append("`),");
        }
        return sb.toString();
    }

    public String getUpdateSql() {
        //todo
        return null;
    }
}
