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
 * @author wangbin
 */
@Getter
@Setter
public class Trigger implements Serializable, Difference {
    private static final long serialVersionUID = 8820673590063935538L;

    @DbUtilProperty("TRIGGER_SCHEMA")
    private String schema;
    @DbUtilProperty("TRIGGER_NAME")
    private String name;
    @DbUtilProperty("DEFINER")
    private String definer;
    @DbUtilProperty("ACTION_TIMING")
    private String actionTiming;
    @DbUtilProperty("EVENT_MANIPULATION")
    private String eventManipulation;
    @DbUtilProperty("EVENT_OBJECT_SCHEMA")
    private String objectSchema;
    @DbUtilProperty("EVENT_OBJECT_TABLE")
    private String objectTable;
    @DbUtilProperty("ACTION_ORIENTATION")
    private String actionOrientation;
    @DbUtilProperty("ACTION_STATEMENT")
    private String source;

    private Database database;

    public static List<Trigger> configure(Connection connection, Database database) throws SQLException {
        return DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/trigger.sql"),
                Trigger.class, database.getInfo().getName())
                .stream().peek(s -> s.setDatabase(database)).collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getCreateSql() {
        return "CREATE TRIGGER " + this.name + " " +
                this.actionTiming + " " + this.eventManipulation + " ON `" + this.objectTable + "` " +
                "FOR EACH " + this.actionOrientation + " " + this.source + ";";
    }

    @JsonIgnore
    @Override
    public String getUpdateSql() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getDeleteSql() {
        return "DROP TRIGGER `" + this.name + "`;";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trigger)) {
            return false;
        }
        Trigger trigger = (Trigger) o;
        return Objects.equals(getName(), trigger.getName()) &&
                Objects.equals(getActionTiming(), trigger.getActionTiming()) &&
                Objects.equals(getEventManipulation(), trigger.getEventManipulation()) &&
                Objects.equals(getObjectTable(), trigger.getObjectTable()) &&
                Objects.equals(getActionOrientation(), trigger.getActionOrientation()) &&
                Objects.equals(getSource(), trigger.getSource());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(),
                getActionTiming(),
                getEventManipulation(),
                getObjectTable(),
                getActionOrientation(),
                getSource());
    }
}
