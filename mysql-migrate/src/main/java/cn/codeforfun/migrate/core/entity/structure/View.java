package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

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
}
