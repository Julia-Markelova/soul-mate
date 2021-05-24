package ifmo.soulmate.demo;

import ifmo.soulmate.demo.background.LifeTicketScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {
    public static void main(String[] args) {
        Class[] sources = {DemoApplication.class, LifeTicketScheduler.class };
        SpringApplication.run(sources, args);
    }

}
