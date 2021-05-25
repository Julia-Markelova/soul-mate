package ifmo.soulmate.demo.repositories;

import ifmo.soulmate.demo.entities.LifeTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILIfeTicketRepository  extends JpaRepository<LifeTicket, Long> {

}
