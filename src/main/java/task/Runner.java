package task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import task.service.FetchDataService;

@SpringBootApplication
public class Runner {

    public static void main(String[] args) {
        ApplicationContext applicationContext =
                SpringApplication.run(Runner.class);

        FetchDataService fetch =
                applicationContext.getBean(FetchDataService.class);
        fetch.getCurrencyLimits();
    }

}
