package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.entities.Life;
import ifmo.soulmate.demo.entities.LifeSpark;
import ifmo.soulmate.demo.entities.LifeTicket;
import ifmo.soulmate.demo.entities.Soul;
import ifmo.soulmate.demo.entities.enums.SoulStatus;
import ifmo.soulmate.demo.repositories.ILIfeTicketRepository;
import ifmo.soulmate.demo.repositories.ILifeSparkRepository;
import ifmo.soulmate.demo.repositories.ILifesRepository;
import ifmo.soulmate.demo.repositories.SoulRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    @Autowired
    SoulRepository soulRepository;
    @Autowired
    ILifesRepository lifesRepository;
    private static final Logger log = LogManager.getLogger(LifeTicketService.class);

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
        Optional<Soul> soul = soulRepository.findById(soulId);
        if (soul.isPresent()) {
            if (soul.get().getStatus() == SoulStatus.UNBORN) {
                LifeTicket ticket = getNotUsedLifeTicket(soulId);
                ticket.setReceiveDate(Date.from(Instant.now()));
                lifeTicketRepository.saveAndFlush(ticket);
                soul.get().setStatus(SoulStatus.BORN);
                Life life = new Life(
                        UUID.randomUUID(),
                        soulId,
                        new Date(),
                        null,
                        "soulName",
                        "soulSurname",
                        0
                );
                lifesRepository.saveAndFlush(life);
                log.info("Soul {} received lifeTicket and turned BORN. New Life {} created", soulId.toString(), life.getId());
            } else {
                String msg = String.format("Expected soul with status UNBORN but got %s", soul.get().getStatus());
                log.warn(msg);
                throw new IllegalArgumentException(msg);
            }
        }
    }

    public void createLifeSpark(UUID soulId, UUID mentorId) {
        LifeSpark lifeSpark = new LifeSpark(
                UUID.randomUUID(),
                null,
                soulId,
                mentorId,
                null
        );
        lifeSparkRepository.saveAndFlush(lifeSpark);
    }
}
