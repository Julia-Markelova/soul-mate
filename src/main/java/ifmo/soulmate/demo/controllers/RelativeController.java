package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.models.MessageDto;
import ifmo.soulmate.demo.services.LifeTicketService;
import ifmo.soulmate.demo.services.LoginService;
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
public class RelativeController {
    @Autowired
    SoulService soulService;

    @Autowired
    LifeTicketService lifeTicketService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    LoginService loginService;


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
