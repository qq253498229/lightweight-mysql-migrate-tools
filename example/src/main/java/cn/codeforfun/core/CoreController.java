package cn.codeforfun.core;

import cn.codeforfun.migrate.core.Migrate;
import cn.codeforfun.migrate.core.diff.DiffResult;
import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileAppender;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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


    @PostMapping("/export")
    public ResponseEntity<Resource> export(@RequestBody DatabaseInfo info) throws SQLException {
        Migrate migrate = new Migrate().ignoreCharacterCompare();
        StringBuilder sql = new StringBuilder();
        List<String> strings = migrate.showSql(info);
        for (String string : strings) {
            sql.append(string).append("\n");
        }
        InputStream inputStream = new ByteArrayInputStream(sql.toString().getBytes(StandardCharsets.UTF_8));

        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource)
                ;
    }

    @PostMapping("/merge")
    public ResponseEntity<Resource> mergeFiles(MultipartFile[] files) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (MultipartFile file : files) {
            String sql = IoUtil.read(file.getInputStream(), StandardCharsets.UTF_8);
            sb.append(sql).append("\n");
        }
        InputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));

        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource)
                ;
    }

    @PostMapping("/exportDifference")
    public ResponseEntity<Resource> exportDifference(@RequestBody Diff diff) throws SQLException {
        Migrate migrate = new Migrate().from(diff.getSource()).to(diff.getTarget());
        migrate.ignoreCharacterCompare();
        DiffResult diffResult = migrate.diff();
        List<String> sqlList = diffResult.getSqlList();
        StringBuilder sb = new StringBuilder();
        for (String s : sqlList) {
            sb.append(s).append("\n");
        }
        InputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));

        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource)
                ;
    }

    @PostMapping("/execute")
    public void executeFiles(@ModelAttribute ExecuteParameter parameter) throws IOException {
        if (CollectionUtils.isEmpty(parameter.getFiles())) {
            return;
        }
        File tempFile = File.createTempFile("test-execute-", ".sql");
        for (MultipartFile file : parameter.getFiles()) {
            FileAppender fileAppender = new FileAppender(tempFile, 16, true);
            String sql = IoUtil.read(file.getInputStream(), StandardCharsets.UTF_8);
            fileAppender.append(sql);
            fileAppender.flush();
        }
        SQLExec sqlExec = new SQLExec();
        sqlExec.setDriver("com.mysql.cj.jdbc.Driver");
        sqlExec.setUrl("jdbc:mysql://" + parameter.getHost() + ":" + parameter.getPort() + "/" + parameter.getName() + "?useUnicode=true&characterEncoding=UTF-8");
        sqlExec.setUserid(parameter.getUsername());
        sqlExec.setPassword(parameter.getPassword());
        sqlExec.setSrc(tempFile);
        sqlExec.setProject(new Project()); // 要指定这个属性，不然会出错
        sqlExec.execute();
        FileUtil.del(tempFile);
    }
}
