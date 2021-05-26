package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.entities.LifeTicket;
import ifmo.soulmate.demo.entities.SoulStatus;
import ifmo.soulmate.demo.models.MessageDto;
import ifmo.soulmate.demo.models.SoulDto;
import ifmo.soulmate.demo.services.LifeTicketService;
import ifmo.soulmate.demo.services.NotificationService;
import ifmo.soulmate.demo.services.SoulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class SoulController {

    @Autowired
    SoulService soulService;

    @Autowired
    LifeTicketService lifeTicketService;

    @Autowired
    NotificationService notificationService;

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

//    todo: сделать нормальный пут-запрос с целой сущностью
    @PutMapping("/souls/{soulId}/update/status/{status}")
    public ResponseEntity updateSoulStatus(@PathVariable String soulId, @PathVariable String status) {
        soulService.updateSoulStatus(UUID.fromString(soulId), SoulStatus.valueOf(status));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/souls/{soulId}/update/mentor/{isMentor}")
    public ResponseEntity updateSoulMentor(@PathVariable String soulId, @PathVariable Boolean isMentor) {
        soulService.updateSoulMentor(UUID.fromString(soulId), isMentor);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications/{relativeId}")
    public ResponseEntity<List<MessageDto>> getNewMessages(@PathVariable String relativeId) {
        return ResponseEntity.ok(notificationService.getNewMessagesForRelative(UUID.fromString(relativeId)));
    }

    @GetMapping("/relatives/{relativeId}/subscriptions")
    public ResponseEntity toggleSubscribe(@PathVariable String relativeId, @RequestParam boolean enable) {
        if (enable) {
            notificationService.subscribe(UUID.fromString(relativeId));
        } else {
            notificationService.unsubscribe(UUID.fromString(relativeId));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/relatives/{relativeId}/subscriptions-status")
    public ResponseEntity<Boolean> getSubscriptionStatus(@PathVariable String relativeId) {
        return ResponseEntity.ok(notificationService.getSubscriptionStatus(UUID.fromString(relativeId)));
    }
}
