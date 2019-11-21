package cn.codeforfun.test;

import cn.codeforfun.utils.StringUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JavaTest {
    @Test
    public void test() {
//        String table = "CREATE TABLE `t_user` (\n" +
//                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                "  `username` varchar(120) NOT NULL COMMENT '用户名',\n" +
//                "  `password` varchar(200) DEFAULT NULL COMMENT '密码',\n" +
//                "  PRIMARY KEY (`id`)\n" +
//                ") ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='用户基础信息表'";
//        List<String> listByPattern = StringUtil.getListByPattern(table, "CREATE TABLE `", "` \\(");
//        System.out.println(listByPattern);
        List<String> listByPattern = StringUtil.getListByPattern("`[a]`,[b],`[a]`", "`\\[", "\\]`");
        System.out.println(listByPattern);
    }
}
