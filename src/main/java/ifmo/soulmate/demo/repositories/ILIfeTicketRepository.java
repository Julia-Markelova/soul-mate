package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.models.LifeTicket;
import ifmo.soulmate.demo.models.Soul;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILIfeTicketRepository  extends JpaRepository<LifeTicket, Long> {
}
