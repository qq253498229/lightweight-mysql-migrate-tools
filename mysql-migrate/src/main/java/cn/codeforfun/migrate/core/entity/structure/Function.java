package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangbin
 */
@Getter
@Setter
public class Function implements Serializable, Difference {
    private static final long serialVersionUID = -1055838976379808321L;

    @DbUtilProperty("DEFINER")
    private String definer;
    @DbUtilProperty("ROUTINE_NAME")
    private String name;
    @DbUtilProperty("ROUTINE_SCHEMA")
    private String schema;
    @DbUtilProperty("DATA_TYPE")
    private String inputType;
    @DbUtilProperty("CHARACTER_MAXIMUM_LENGTH")
    private Integer maxLength;
    @DbUtilProperty("NUMERIC_PRECISION")
    private Integer numberLength;
    @DbUtilProperty("DTD_IDENTIFIER")
    private String resultType;
    @DbUtilProperty("ROUTINE_DEFINITION")
    private String source;
    @DbUtilProperty("SECURITY_TYPE")
    private String securityType;


    @Override
    public String getCreateSql() {
        StringBuilder sb = new StringBuilder();
        String[] split = this.definer.split("@");
        sb.append("CREATE DEFINER = `").append(split[0]).append("`@`").append(split[1]).append("`");
        sb.append(" FUNCTION `").append("`");
        return sb.toString();
    }

    @Override
    public String getUpdateSql() {
        return null;
    }

    @Override
    public String getDeleteSql() {
        return "DROP FUNCTION `" + this.name + "`;";
    }
}
