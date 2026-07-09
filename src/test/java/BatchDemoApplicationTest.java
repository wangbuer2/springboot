import chong.wang.Main;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@EnableBatchProcessing
@SpringBootTest(classes = Main.class)
public class BatchDemoApplicationTest {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job userJob;

    @Test
    void testRunBatchJob() throws Exception {
        // Job参数必须唯一，否则springbatch认为任务已执行不会重复跑
        JobParameters params = new JobParametersBuilder()
                .addLong("runTime", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(userJob, params);
        System.out.println("任务执行状态：" + execution.getStatus());
    }
}
