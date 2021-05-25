package ifmo.soulmate.demo.background;

import ifmo.soulmate.demo.models.LifeSpark;
import ifmo.soulmate.demo.models.LifeTicket;
import ifmo.soulmate.demo.repositories.ILIfeTicketRepository;
import ifmo.soulmate.demo.repositories.ILifeSparkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LifeTicketScheduler {

    @Autowired
    ILifeSparkRepository lifeSparkRepository;
    @Autowired
    ILIfeTicketRepository lifeTicketRepository;

    @Scheduled(cron = "*/10 * * * * *")
    private void schedule() {
        List<LifeSpark> allSparks = lifeSparkRepository.findAll();
        List<LifeTicket> allTickets = lifeTicketRepository.findAll();

        List<LifeSpark> filteredSparks = allSparks
                .stream()
                .filter((spark) -> allTickets
                        .stream()
                        .noneMatch(t -> t.getLife_spark_id() == spark.getId()))
                .collect(Collectors.toList());

        for (LifeSpark spark: filteredSparks) {
            LifeTicket ticket = new LifeTicket(UUID.randomUUID(), Date.from(Instant.now()), spark.getId(), true);
            lifeTicketRepository.saveAndFlush(ticket);
        }
    }
}
