package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author wangbin
 */
@Getter
@Setter
public class Routine {

    @DbUtilProperty("SECURITY_TYPE")
    private String securityType;
    @DbUtilProperty("DEFINER")
    private String definer;
    @DbUtilProperty("ROUTINE_SCHEMA")
    private String schema;
    @DbUtilProperty("SPECIFIC_NAME")
    private String name;
    @DbUtilProperty("ROUTINE_DEFINITION")
    private String source;

    @DbUtilProperty("PARAMETER_MODE")
    private String paramMode;
    @DbUtilProperty("PARAMETER_NAME")
    private String paramName;
    @DbUtilProperty("DATA_TYPE")
    private String paramType;
    @DbUtilProperty("CHARACTER_MAXIMUM_LENGTH")
    private Integer characterLength;
    @DbUtilProperty("NUMERIC_PRECISION")
    private Integer numberLength;
    @DbUtilProperty("NUMERIC_SCALE")
    private Integer column;
    @DbUtilProperty("DATETIME_PRECISION")
    private String datePrecision;
    @DbUtilProperty("DTD_IDENTIFIER")
    private String resultType;
    @DbUtilProperty("CHARACTER_SET_NAME")
    private String character;
    @DbUtilProperty("COLLATION_NAME")
    private String collation;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Routine)) {
            return false;
        }
        Routine that = (Routine) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getSource(), that.getSource()) &&
                Objects.equals(getParamMode(), that.getParamMode()) &&
                Objects.equals(getParamName(), that.getParamName()) &&
                Objects.equals(getParamType(), that.getParamType()) &&
                Objects.equals(getCharacterLength(), that.getCharacterLength()) &&
                Objects.equals(getNumberLength(), that.getNumberLength()) &&
                Objects.equals(getColumn(), that.getColumn()) &&
                Objects.equals(getDatePrecision(), that.getDatePrecision()) &&
                Objects.equals(getResultType(), that.getResultType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getName(),
                getSource(),
                getParamMode(),
                getParamName(),
                getParamType(),
                getCharacterLength(),
                getNumberLength(),
                getColumn(),
                getDatePrecision(),
                getResultType());
    }
}
