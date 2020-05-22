package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * View结构定义
 *
 * @author wangbin
 */
@Getter
@Setter
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

    public static List<View> configure(Connection connection, String databaseName) throws SQLException {
        return DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/view.sql"),
                View.class, databaseName);
    }

    @JsonIgnore
    @Override
    public String getCreateSql() {
        return getString("CREATE ");
    }

    @JsonIgnore
    @Override
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

    @JsonIgnore
    @Override
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
        return Objects.equals(getName(), view.getName()) &&
                Objects.equals(getSource(), view.getSource()) &&
                Objects.equals(getCheckOption(), view.getCheckOption())
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSource(), getDefiner(), getSecurityType(), getCharacter(), getCollation(), getCheckOption(), getUpdatable());
    }
}
