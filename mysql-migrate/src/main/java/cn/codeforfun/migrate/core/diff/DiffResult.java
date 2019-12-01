package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.DatabaseStructure;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
public class DiffResult {

    private DatabaseStructure from;
    private DatabaseStructure to;

    private List<Difference> createDifferenceList = new ArrayList<>();
    private List<Difference> deleteDifferenceList = new ArrayList<>();
    private List<Difference> updateDifferenceList = new ArrayList<>();


    public String getSQL() {
        return null;
    }

    public DiffResult compare(DatabaseStructure from, DatabaseStructure to) {
        //todo
        return new DiffResult();
    }


}
