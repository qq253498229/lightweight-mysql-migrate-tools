package cn.codeforfun.core.entity.structure;

import cn.codeforfun.core.entity.structure.utils.ConvertUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangbin
 */
@Getter
@Setter
public class Column {
    private String name;
    private String type;
    private String defaultValue;
    private String comment;

    private Boolean notNull;
    private Boolean autoIncrement;
    private Boolean unique;
    private Boolean primaryKey;

    public static List<Column> configure(String tableStructure) {
        String columns = ConvertUtil.tableColumns(tableStructure);
        return null;
    }
}
