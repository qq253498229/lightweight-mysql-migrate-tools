package cn.codeforfun.core;

import cn.codeforfun.migrate.core.Migrate;
import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import cn.hutool.core.io.IoUtil;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class ResolveController {
    @GetMapping("/resolve")
    public ModelAndView resolve() {
        return new ModelAndView("resolve");
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

    @PostMapping("/execute")
    public void executeFiles(@ModelAttribute ExecuteParameter parameter) throws IOException, SQLException {
        if (CollectionUtils.isEmpty(parameter.getFiles())) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (MultipartFile file : parameter.getFiles()) {
            String sql = IoUtil.read(file.getInputStream(), StandardCharsets.UTF_8);
            sb.append(sql).append("\n");
        }
        String sql = sb.toString();
        sql = Pattern.compile("^--(.*)$\r\n", Pattern.MULTILINE).matcher(sql).replaceAll("");
        sql = Pattern.compile("^/\\*!(.*)\\*/;$\r\n", Pattern.MULTILINE).matcher(sql).replaceAll("");
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(parameter.getUsername());
        druidDataSource.setPassword(parameter.getPassword());
        druidDataSource.setUrl("jdbc:mysql://" + parameter.getHost() + ":" + parameter.getPort() + "/" + parameter.getName() + "?useUnicode=true&characterEncoding=UTF-8");
        Connection connection = druidDataSource.getConnection();
        String[] split = sql.replaceAll("\\r", "").replaceAll("\\n", "")
                .split("\\s*;\\s*(?=([^']*'[^']*')*[^']*$)");
        Statement statement = connection.createStatement();
        for (String s : split) {
            statement.addBatch(s);
        }
        statement.executeBatch();
    }
}
