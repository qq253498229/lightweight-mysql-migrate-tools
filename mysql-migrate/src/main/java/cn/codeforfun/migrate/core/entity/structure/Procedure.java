package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Procedure结构定义
 *
 * @author wangbin
 */
@Getter
@Setter
public class Procedure implements Serializable, Difference {
    private static final long serialVersionUID = -7316847891519066690L;

    private String securityType;
    private String definer;
    private String schema;
    private String name;
    private String source;

    private Database database;

    private List<Routine> routines = new ArrayList<>();

    public static List<Procedure> configure(Connection connection, Database database) throws SQLException {
        List<Routine> beanList = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/procedure.sql"),
                Routine.class, database.getInfo().getName());
        Map<String, Procedure> procedures = new HashMap<>(0);
        for (Routine routine : beanList) {
            Procedure procedure = procedures.get(routine.getName());
            if (procedure == null) {
                procedure = new Procedure();
            }
            procedure.setDatabase(database);
            procedure.getRoutines().add(routine);
            procedure.setDefiner(routine.getDefiner());
            procedure.setSecurityType(routine.getSecurityType());
            procedure.setSchema(routine.getSchema());
            procedure.setName(routine.getName());
            procedure.setSource(routine.getSource());
            procedures.put(routine.getName(), procedure);
        }
        return new ArrayList<>(procedures.values());
    }

    @JsonIgnore
    @Override
    public String getCreateSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE PROCEDURE `").append(this.name).append("`");
        sb.append("(");
        List<Routine> inputTypeList = this.getRoutines().stream().filter(s -> "IN".equals(s.getParamMode())).collect(Collectors.toList());
        List<Routine> resultTypeList = this.getRoutines().stream().filter(s -> "OUT".equals(s.getParamMode())).collect(Collectors.toList());
        for (Routine inList : inputTypeList) {
            sb.append("IN ").append(inList.getParamName()).append(" ").append(inList.getResultType()).append(",");
        }
        for (Routine outList : resultTypeList) {
            sb.append("OUT ").append(outList.getParamName()).append(" ").append(outList.getResultType()).append(",");
        }
        if (inputTypeList.size() > 0 || resultTypeList.size() > 0) {
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        }
        sb.append(") ");
        sb.append(" ").append(this.source).append(";");
        return sb.toString();
    }

    @JsonIgnore
    @Override
    public String getUpdateSql() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getDeleteSql() {
        return "DROP PROCEDURE `" + this.name + "`;";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Procedure)) {
            return false;
        }
        Procedure procedure = (Procedure) o;
        return Objects.equals(getName(), procedure.getName()) &&
                Objects.equals(getSource(), procedure.getSource()) &&
                Objects.equals(getRoutines(), procedure.getRoutines());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(),
                getSource(),
                getRoutines());
    }
}
