package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangbin
 */
@Getter
@Setter
@EqualsAndHashCode
public class Key implements Serializable {
    private static final long serialVersionUID = 1097940296556989104L;

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

}
