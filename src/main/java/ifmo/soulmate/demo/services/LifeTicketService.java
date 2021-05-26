package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.entities.LifeSpark;
import ifmo.soulmate.demo.entities.LifeTicket;
import ifmo.soulmate.demo.repositories.ILIfeTicketRepository;
import ifmo.soulmate.demo.repositories.ILifeSparkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LifeTicketService {

    @Autowired
    ILifeSparkRepository lifeSparkRepository;
    @Autowired
    ILIfeTicketRepository lifeTicketRepository;

    public List<LifeSpark> getSparksWithoutTickets() {
        List<LifeSpark> allSparks = lifeSparkRepository.findAll();
        List<LifeTicket> allTickets = lifeTicketRepository.findAll();

        return allSparks
                .stream()
                .filter((spark) -> allTickets
                        .stream()
                        .noneMatch(t -> t.getLife_spark_id().equals(spark.getId())))
                .collect(Collectors.toList());
    }


    public LifeTicket getNotUsedLifeTicket(UUID soulId) {
        List<LifeSpark> allSparks = lifeSparkRepository.findAll();
        List<LifeTicket> allTickets = lifeTicketRepository.findAll();

        Optional<LifeTicket> ticket = allTickets
                .stream()
                .filter(t -> allSparks
                        .stream()
                        .anyMatch(s -> s.getReceived_by().equals(soulId) &&
                                s.getId().equals(t.getLife_spark_id()) && t.getReceiveDate() == null))
                .findFirst();
        return ticket.orElse(null);
    }

    public void receiveLifeTicket(UUID soulId, UUID ticketId) {
        LifeTicket ticket = getNotUsedLifeTicket(soulId);
        ticket.setReceiveDate(Date.from(Instant.now()));
        lifeTicketRepository.saveAndFlush(ticket);
    }
}
