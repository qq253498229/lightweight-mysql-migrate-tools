package cn.codeforfun.migrate.core.entity.structure;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wangbin
 */
@Getter
@Setter
public class ForeignKey implements Serializable {
    private static final long serialVersionUID = 8968786559440364972L;

    private String name;
    private String targetTable;

    /**
     * todo
     */
    private UpdateRule updateRule;
    /**
     * todo
     */
    private DeleteRule deleteRule;

    class UpdateRule {

    }

    class DeleteRule {

    }
}
