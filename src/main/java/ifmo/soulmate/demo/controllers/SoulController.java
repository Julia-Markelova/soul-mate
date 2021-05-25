package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.entities.LifeTicket;
import ifmo.soulmate.demo.models.SoulDto;
import ifmo.soulmate.demo.services.LifeTicketService;
import ifmo.soulmate.demo.services.SoulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class SoulController {

    @Autowired
    SoulService soulService;

    @Autowired
    LifeTicketService lifeTicketService;

    @GetMapping("/souls")
    public ResponseEntity<List<SoulDto>> getAllSouls() {
        return ResponseEntity.ok(soulService.getSouls());
    }

    @GetMapping("/souls/{id}/life-tickets")
    public ResponseEntity<LifeTicket> getLifeTicket(@PathVariable String id) {
        return ResponseEntity.ok(lifeTicketService.getNotUsedLifeTicket(UUID.fromString(id)));
    }

    @PutMapping("/souls/{soulId}/life-tickets/{ticketId}")
    public ResponseEntity receiveLifeTicket(@PathVariable String soulId, @PathVariable String ticketId) {
        lifeTicketService.receiveLifeTicket(UUID.fromString(soulId), UUID.fromString(ticketId));
        return ResponseEntity.ok().build();
    }
}
