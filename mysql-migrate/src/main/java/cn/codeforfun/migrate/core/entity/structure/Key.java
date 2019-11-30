package cn.codeforfun.migrate.core.entity.structure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangbin
 */
@Getter
@Setter
public class Key implements Serializable {
    private static final long serialVersionUID = 1097940296556989104L;

    private String name;
    private boolean isPrimaryKey;
}
