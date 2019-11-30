package cn.codeforfun.core.entity.structure;

import cn.codeforfun.core.entity.Database;
import cn.codeforfun.core.exception.DatabaseReadException;
import cn.codeforfun.utils.DbUtil;
import cn.codeforfun.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
public class DatabaseStructure {
    private Database database;

    private List<Table> tables;

    private String name;
    private String character;
    private String collate;

    private Connection connection;

    public void config(Database database) {
        this.database = database;
        this.connection = DbUtil.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        configDatabaseStructure();
        this.tables = Table.configure(connection);
    }

    private void configDatabaseStructure() {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("show create database " + database.getName());
            String createDatabaseSql = null;
            if (rs.next()) {
                int columnCount = rs.getMetaData().getColumnCount();
                createDatabaseSql = rs.getString(columnCount);
            }
            match(createDatabaseSql);
        } catch (SQLException e) {
            log.error("读取数据库失败,url:{},user:{},password:{}", database.getUrl(), database.getUsername(), database.getPassword());
            throw new DatabaseReadException("读取数据库失败");
        }
    }

    private void match(String structureSql) {
        String regex = "\\s*create\\s+database\\s+(?<name>\\S+)+[\\s\\S]+character\\s+set\\s+(?<character>\\S+)\\s+collate\\s+(?<collate>\\S+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(structureSql);
        if (matcher.find()) {
            this.name = StringUtil.deleteDot(matcher.group("name"));
            this.character = matcher.group("character");
            this.collate = matcher.group("collate");
        }
    }
}
