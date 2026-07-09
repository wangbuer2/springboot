package config;

import lombok.SneakyThrows;
import model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing // 自动装配JobRepository、JobLauncher、Step工厂
public class BatchConfig {
    @Resource
    private JobBuilderFactory jobBuilderFactory;

    @Resource
    private StepBuilderFactory stepBuilderFactory;


    // 1. Reader：模拟内存数据源读取
    @SneakyThrows
    @Bean
    public ItemReader<User> userReader() {
        List<User> userList = new ArrayList<>();
        User u1 = new User();
        u1.setName("张三");
        u1.setAge(18);
//        u1.wait(1L);

        User u2 = new User();
//        u2.wait(2L);
        u2.setName("李四");
        u2.setAge(22);

        User u3 = new User();
//        u3.wait(3L);
        u3.setName("王五");
        u3.setAge(16);

        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        return new ListItemReader<>(userList);
    }

    // 2. Processor：数据处理、过滤
    @Bean
    public ItemProcessor<User, User> userProcessor() {
        return user -> {
            System.out.println("处理器原始数据：" + user);
            // 过滤未成年，返回null则丢弃该条数据
            if (user.getAge() < 18) {
                return null;
            }
            // 数据转换
            user.setName(user.getName() + "_成年");
            return user;
        };
    }

    // 3. Writer：输出处理后数据（控制台打印）
    @Bean
    public ItemWriter<User> userWriter() {
        return items -> {
            System.out.println("===== 批量写入chunk数据 =====");
            for (User item : items) {
                System.out.println(item);
            }
        };
    }

    // 4. Step：组装读-处理-写，chunk=2，每2条一个事务
    @Bean
    public Step userStep() {
        return stepBuilderFactory.get("userStep")
                .<User, User>chunk(2)
                .reader(userReader())
                .processor(userProcessor())
                .writer(userWriter())
                .build();
    }

    // 5. Job：绑定步骤
    @Bean
    public Job userJob() {
        return jobBuilderFactory.get("userJob")
                .start(userStep())
                .build();
    }
}
