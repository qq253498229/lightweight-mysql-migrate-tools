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

    public static List<Trigger> configure(Connection connection, String databaseName) throws SQLException {
        return DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/trigger.sql"),
                Trigger.class, databaseName);
    }

    @JsonIgnore
    @Override
    public String getCreateSql() {
        String[] split = this.definer.split("@");
        return "CREATE DEFINER =`" + split[0] + "`@`" + split[1] + "` " +
                "TRIGGER " + this.name + " " +
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
        return Objects.equals(getSchema(), trigger.getSchema()) &&
                Objects.equals(getName(), trigger.getName()) &&
                Objects.equals(getDefiner(), trigger.getDefiner()) &&
                Objects.equals(getActionTiming(), trigger.getActionTiming()) &&
                Objects.equals(getEventManipulation(), trigger.getEventManipulation()) &&
                Objects.equals(getObjectSchema(), trigger.getObjectSchema()) &&
                Objects.equals(getObjectTable(), trigger.getObjectTable()) &&
                Objects.equals(getActionOrientation(), trigger.getActionOrientation()) &&
                Objects.equals(getSource(), trigger.getSource());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSchema(),
                getName(),
                getDefiner(),
                getActionTiming(),
                getEventManipulation(),
                getObjectSchema(),
                getObjectTable(),
                getActionOrientation(),
                getSource());
    }
}
