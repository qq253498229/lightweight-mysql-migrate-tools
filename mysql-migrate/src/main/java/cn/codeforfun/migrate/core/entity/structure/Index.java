package cn.codeforfun.migrate.core.entity.structure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangbin
 */
@Getter
@Setter
public class Index implements Serializable {
    private static final long serialVersionUID = 742409976263713331L;

    private String name;
    private Boolean unique;

}
