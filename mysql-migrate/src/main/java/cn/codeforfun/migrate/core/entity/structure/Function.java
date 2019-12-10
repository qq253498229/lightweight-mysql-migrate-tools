package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class Function implements Serializable, Difference {
    private static final long serialVersionUID = -1055838976379808321L;

    private String securityType;
    private String definer;
    private String schema;
    private String name;
    private String source;

    private List<FunctionParam> functionParams = new ArrayList<>();

    public static List<Function> configure(Connection connection, String databaseName) throws SQLException {
        List<FunctionParam> beanList = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/function.sql"),
                FunctionParam.class, databaseName);
        Map<String, Function> functions = new HashMap<>(0);
        for (FunctionParam param : beanList) {
            Function function = functions.get(param.getName());
            if (function == null) {
                function = new Function();
            }
            function.getFunctionParams().add(param);
            function.setDefiner(param.getDefiner());
            function.setSecurityType(param.getSecurityType());
            function.setSchema(param.getSchema());
            function.setName(param.getName());
            function.setSource(param.getSource());
            functions.put(param.getName(), function);
        }
        return new ArrayList<>(functions.values());
    }

    @Override
    public String getCreateSql() {
        //fixme
        StringBuilder sb = new StringBuilder();
        String[] split = this.definer.split("@");
        sb.append("CREATE DEFINER = `").append(split[0]).append("`@`").append(split[1]).append("`");
        sb.append(" FUNCTION `").append(this.name).append("`");
        sb.append("(");
        List<FunctionParam> inputTypeList = this.getFunctionParams().stream().filter(s -> "IN".equals(s.getParamMode())).collect(Collectors.toList());
        List<FunctionParam> resultTypeList = this.getFunctionParams().stream().filter(s -> null == s.getParamMode()).collect(Collectors.toList());
        for (FunctionParam param : inputTypeList) {
            sb.append(param.getParamName()).append(" ").append(param.getResultType()).append(",");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        sb.append(")");
        sb.append(" RETURNS ");
        FunctionParam param = resultTypeList.get(0);
        sb.append(param.getResultType());
        if (!ObjectUtils.isEmpty(param.getCharacter()) && !ObjectUtils.isEmpty(param.getCollation())) {
            sb.append(" CHARSET ").append(param.getCharacter()).append(" COLLATE ").append(param.getCollation());
        }
        sb.append(" ").append(this.source).append(";");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Function)) {
            return false;
        }
        Function function = (Function) o;
        return Objects.equals(getSecurityType(), function.getSecurityType()) &&
                Objects.equals(getDefiner(), function.getDefiner()) &&
                Objects.equals(getSchema(), function.getSchema()) &&
                Objects.equals(getName(), function.getName()) &&
                Objects.equals(getSource(), function.getSource()) &&
                Objects.equals(getFunctionParams(), function.getFunctionParams());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSecurityType(),
                getDefiner(),
                getSchema(),
                getName(),
                getSource(),
                getFunctionParams());
    }

}
