package ifmo.soulmate.demo.background;

import ifmo.soulmate.demo.entities.*;
import ifmo.soulmate.demo.entities.enums.SystemModeType;
import ifmo.soulmate.demo.repositories.ILIfeTicketRepository;
import ifmo.soulmate.demo.repositories.ILifeSparkRepository;
import ifmo.soulmate.demo.repositories.SystemModeRepository;
import ifmo.soulmate.demo.services.LifeTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class LifeTicketScheduler {

    @Autowired
    LifeTicketService lifeTicketService;
    @Autowired
    ILIfeTicketRepository lifeTicketRepository;
    @Autowired
    SystemModeRepository systemModeRepository;
    @Autowired
    ILifeSparkRepository lifeSparkRepository;

    @Scheduled(cron = "*/30 * * * * *")
    private void schedule() {
        SystemMode ticketMode = systemModeRepository.findSystemModeByType(SystemModeType.LIFE_TICKET_MODE);
        SystemMode sparkMode = systemModeRepository.findSystemModeByType(SystemModeType.LIFE_SPARK_MODE);

        if (!sparkMode.getIsManualMode()) {
            List<Soul> soulsWithoutSparks = lifeTicketService.getSoulsWithoutSparks();

            for (Soul soul : soulsWithoutSparks) {
                lifeTicketService.createLifeSpark(soul.getId(), soul.getId());
                System.out.println("Создана искра жизни для души " + soul.getId());
            }
            if (soulsWithoutSparks.isEmpty()) {
                System.out.println("Не обнаруено душ, которым можно выдать искру жизни");
            }
        } else {
            List<PersonalProgram> finishedProgramsWithoutSparks = lifeTicketService.getFinishedProgramsWithoutSparks();
            for (PersonalProgram program : finishedProgramsWithoutSparks) {
                LifeSpark spark = new LifeSpark(UUID.randomUUID(), new Date(), program.getSoulId(), null, program.getId());
                lifeSparkRepository.saveAndFlush(spark);
                System.out.println("Создана искра жизни по завершении персональной программы для души " + program.getSoulId());
            }
            if (finishedProgramsWithoutSparks.isEmpty()) {
                System.out.println("Не обнаружено новых завершенных персональных программ.");
            }

        }

        if (!ticketMode.getIsManualMode()) {
            List<LifeSpark> filteredSparks = lifeTicketService.getSparksWithoutTickets();

            for (LifeSpark spark : filteredSparks) {
                LifeTicket ticket = new LifeTicket(UUID.randomUUID(), null, spark.getId(), true);
                lifeTicketRepository.saveAndFlush(ticket);
                System.out.println("Создан билет в жизнь для души " + spark.getReceived_by());
            }
            if (filteredSparks.isEmpty()) {
                System.out.println("Не обнаруено душ, которым можно выдать билет в жизнь");
            }
        }
    }
}
