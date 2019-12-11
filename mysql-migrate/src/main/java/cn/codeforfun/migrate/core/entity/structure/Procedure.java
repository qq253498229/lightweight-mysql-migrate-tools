package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class Procedure implements Serializable, Difference {
    private static final long serialVersionUID = -7316847891519066690L;

    private String securityType;
    private String definer;
    private String schema;
    private String name;
    private String source;

    private List<Routine> routines = new ArrayList<>();

    public static List<Procedure> configure(Connection connection, String databaseName) throws SQLException {
        List<Routine> beanList = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/procedure.sql"),
                Routine.class, databaseName);
        Map<String, Procedure> procedures = new HashMap<>(0);
        for (Routine routine : beanList) {
            Procedure procedure = procedures.get(routine.getName());
            if (procedure == null) {
                procedure = new Procedure();
            }
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

    @Override
    public String getCreateSql() {
        StringBuilder sb = new StringBuilder();
        String[] split = this.definer.split("@");
        sb.append("CREATE DEFINER = `").append(split[0]).append("`@`").append(split[1]).append("`");
        sb.append(" PROCEDURE `").append(this.name).append("`");
        sb.append("(");
        List<Routine> inputTypeList = this.getRoutines().stream().filter(s -> "IN".equals(s.getParamMode())).collect(Collectors.toList());
        List<Routine> resultTypeList = this.getRoutines().stream().filter(s -> "OUT".equals(s.getParamMode())).collect(Collectors.toList());
        for (Routine inList : inputTypeList) {
            sb.append("IN ").append(inList.getParamName()).append(" ").append(inList.getResultType()).append(",");
        }
        for (Routine outList : resultTypeList) {
            sb.append("OUT ").append(outList.getParamName()).append(" ").append(outList.getResultType()).append(",");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        sb.append(")\n");
        sb.append(this.source).append(";");
        return sb.toString();
    }

    @Override
    public String getUpdateSql() {
        return null;
    }

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
        return Objects.equals(getSecurityType(), procedure.getSecurityType()) &&
                Objects.equals(getDefiner(), procedure.getDefiner()) &&
                Objects.equals(getSchema(), procedure.getSchema()) &&
                Objects.equals(getName(), procedure.getName()) &&
                Objects.equals(getSource(), procedure.getSource()) &&
                Objects.equals(getRoutines(), procedure.getRoutines());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSecurityType(),
                getDefiner(),
                getSchema(),
                getName(),
                getSource(),
                getRoutines());
    }
}
