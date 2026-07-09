package chong.wang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("config")
public class Main {
    // 定义日志对象
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String str = "this is test";
        log.info(str);
        System.out.printf(str);
        SpringApplication.run(Main.class, args);
    }
}