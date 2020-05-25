package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Function结构定义
 *
 * @author wangbin
 */
@Getter
@Setter
public class Function implements Serializable, Difference {
    private static final long serialVersionUID = -1055838976379808321L;

    private String securityType;
    private String definer;
    private String schema;
    private String name;
    private String source;

    private List<Routine> routines = new ArrayList<>();

    public static List<Function> configure(Connection connection, String databaseName) throws SQLException {
        List<Routine> beanList = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/function.sql"),
                Routine.class, databaseName);
        Map<String, Function> functions = new HashMap<>(0);
        for (Routine routine : beanList) {
            Function function = functions.get(routine.getName());
            if (function == null) {
                function = new Function();
            }
            function.getRoutines().add(routine);
            function.setDefiner(routine.getDefiner());
            function.setSecurityType(routine.getSecurityType());
            function.setSchema(routine.getSchema());
            function.setName(routine.getName());
            function.setSource(routine.getSource());
            functions.put(routine.getName(), function);
        }
        return new ArrayList<>(functions.values());
    }

    @JsonIgnore
    @Override
    public String getCreateSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE FUNCTION `").append(this.name).append("`");
        sb.append("(");
        List<Routine> inputTypeList = this.getRoutines().stream().filter(s -> "IN".equals(s.getParamMode())).collect(Collectors.toList());
        List<Routine> resultTypeList = this.getRoutines().stream().filter(s -> null == s.getParamMode()).collect(Collectors.toList());
        for (Routine param : inputTypeList) {
            sb.append(param.getParamName()).append(" ").append(param.getResultType()).append(",");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        sb.append(") ");
        sb.append(" RETURNS ");
        Routine param = resultTypeList.get(0);
        sb.append(param.getResultType());
        if (!ObjectUtils.isEmpty(param.getCharacter()) && !ObjectUtils.isEmpty(param.getCollation())) {
            sb.append(" CHARSET ").append(param.getCharacter()).append(" COLLATE ").append(param.getCollation());
        }
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
        return "DROP FUNCTION `" + this.name + "`;";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Function)) {
            return false;
        }
        Function function = (Function) o;
        return
                Objects.equals(getName(), function.getName()) &&
                        Objects.equals(getSource(), function.getSource()) &&
                        Objects.equals(getRoutines(), function.getRoutines());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(),
                getSource(),
                getRoutines());
    }

}
