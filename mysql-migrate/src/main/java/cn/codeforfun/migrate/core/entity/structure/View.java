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
import java.util.stream.Collectors;

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

    private Database database;

    public static List<View> configure(Connection connection, Database database) throws SQLException {
        return DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/view.sql"),
                View.class, database.getInfo().getName())
                .stream().peek(s -> s.setDatabase(database)).collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getCreateSql() {
        return getString("CREATE ");
    }

    @JsonIgnore
    @Override
    public String getUpdateSql() {
        return getString("CREATE OR REPLACE ");
    }

    private String getString(String type) {
        return type +
                "VIEW `" + this.name + "` AS " +
                this.source + ";";
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
        return Objects.hash(getName(),
                getSource(),
                getCheckOption());
    }
}
