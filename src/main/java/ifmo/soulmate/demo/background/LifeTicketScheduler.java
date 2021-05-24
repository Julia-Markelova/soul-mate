package ifmo.soulmate.demo.background;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class LifeTicketScheduler {

    @Scheduled(cron = "*/10 * * * * *")
    private void schedule() {
        System.out.println("hello world");
    }
}
