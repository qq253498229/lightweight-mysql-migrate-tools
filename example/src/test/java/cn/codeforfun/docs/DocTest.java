package cn.codeforfun.docs;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.junit.jupiter.api.Test;

import java.io.File;

public class DocTest {
    @Test
    public void name() {
        File parentFile = new File(this.getClass().getResource("/").getFile()).getParentFile().getParentFile().getParentFile();
        String docPath = parentFile.getPath() + File.separator + "README.adoc";
        String toPath = parentFile.getPath() + File.separator + "docs" + File.separator + "index.html";
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        Options options = Options.builder().toFile(new File(toPath)).safe(SafeMode.UNSAFE).build();
        asciidoctor.convertFile(new File(docPath), options);
    }

}
