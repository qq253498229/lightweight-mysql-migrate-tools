package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
public class View implements Serializable, Difference {
    private static final long serialVersionUID = 1007279552599004329L;
    @DbUtilProperty("TABLE_SCHEMA")
    private String schema;
    @DbUtilProperty("TABLE_NAME")
    private String name;
    @DbUtilProperty("VIEW_DEFINITION")
    private String source;
    @DbUtilProperty("DEFINER")
    private String definer;
    @DbUtilProperty("SECURITY_TYPE")
    private String securityType;
    @DbUtilProperty("CHARACTER_SET_CLIENT")
    private String character;
    @DbUtilProperty("COLLATION_CONNECTION")
    private String collation;

    @DbUtilProperty("CHECK_OPTION")
    private String checkOption;
    @DbUtilProperty("IS_UPDATABLE")
    private String updatable;

    public String getCreateSql() {
        return getString("CREATE ");
    }

    public String getUpdateSql() {
        return getString("ALTER ");
    }

    private String getString(String type) {
        StringBuilder sb = new StringBuilder();
        String[] split = this.definer.split("@");
        sb.append(type);
        sb.append("DEFINER =`").append(split[0]).append("`@`").append(split[1]).append("` ");
        sb.append("SQL SECURITY ").append(this.securityType).append(" ");
        sb.append("VIEW `").append(this.name).append("` AS ");
        sb.append(this.source);
        return sb.toString();
    }

    public String getDeleteSql() {
        return "drop view `" + this.name + "`;";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof View)) {
            return false;
        }
        View view = (View) o;
        return Objects.equals(getSchema(), view.getSchema()) &&
                Objects.equals(getName(), view.getName()) &&
                Objects.equals(getSource(), view.getSource()) &&
                Objects.equals(getDefiner(), view.getDefiner()) &&
                Objects.equals(getSecurityType(), view.getSecurityType()) &&
                Objects.equals(getCharacter(), view.getCharacter()) &&
                Objects.equals(getCollation(), view.getCollation()) &&
                Objects.equals(getCheckOption(), view.getCheckOption()) &&
                Objects.equals(getUpdatable(), view.getUpdatable());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSchema(), getName(), getSource(), getDefiner(), getSecurityType(), getCharacter(), getCollation(), getCheckOption(), getUpdatable());
    }
}
