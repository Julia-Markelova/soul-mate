package ifmo.soulmate.demo.background;

import ifmo.soulmate.demo.entities.LifeSpark;
import ifmo.soulmate.demo.entities.LifeTicket;
import ifmo.soulmate.demo.repositories.ILIfeTicketRepository;
import ifmo.soulmate.demo.services.LifeTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LifeTicketScheduler {

    @Autowired
    LifeTicketService lifeTicketService;
    @Autowired
    ILIfeTicketRepository lifeTicketRepository;

    @Scheduled(cron = "*/10 * * * * *")
    private void schedule() {
        List<LifeSpark> filteredSparks = lifeTicketService.getSparksWithoutTickets();

        for (LifeSpark spark: filteredSparks) {
            LifeTicket ticket = new LifeTicket(UUID.randomUUID(), null, spark.getId(), true);
            lifeTicketRepository.saveAndFlush(ticket);
        }
    }
}
