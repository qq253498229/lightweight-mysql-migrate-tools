package cn.codeforfun.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ExecuteParameter {
    private String host;
    private Integer port;
    private String username;
    private String password;

    private String name;

    private List<MultipartFile> files;
}
