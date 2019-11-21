package cn.codeforfun.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangbin
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public String test() {
        return "hello, world";
    }
}
