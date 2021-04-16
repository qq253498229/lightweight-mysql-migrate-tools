package cn.codeforfun.core;

import cn.codeforfun.migrate.core.Migrate;
import cn.codeforfun.migrate.core.diff.DiffResult;
import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wangbin
 */
@RestController
public class CoreController {
    /**
     * 渲染html页面
     */
    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    /**
     * 对比数据库
     */
    @PostMapping("/diff")
    public List<String> diff(@RequestBody Diff diff) throws SQLException {
        Migrate migrate = new Migrate().from(diff.getSource()).to(diff.getTarget());
        migrate.ignoreCharacterCompare();
        DiffResult diffResult = migrate.diff();
        return diffResult.getSqlList();
    }

    @PostMapping("/showSql")
    public List<String> showSql(@RequestBody DatabaseInfo info) throws SQLException {
        Migrate migrate = new Migrate().ignoreCharacterCompare();
        return migrate.showSql(info);
    }

    /**
     * 更新数据库
     */
    @PostMapping("/update")
    public void update(@RequestBody Diff diff) throws SQLException {
        Migrate migrate = new Migrate().from(diff.getSource()).to(diff.getTarget());
        migrate.update();
    }
}
