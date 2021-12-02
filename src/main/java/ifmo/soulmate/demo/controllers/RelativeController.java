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


//    @GetMapping("/relatives/get-new-messages")
//    public ResponseEntity<List<MessageDto>> getNewMessages(@RequestHeader("soul-token") String token) {
//// todo
//        return ResponseEntity.ok(notificationService.getNewMessagesForRelative(UUID.fromString(relativeId)));
//    }
//
//    @GetMapping("/relatives/toggle-subscriptions")
//    // todo
//    public ResponseEntity toggleSubscribe(@RequestHeader("soul-token") String token, @RequestParam boolean enable) {
//        if (enable) {
//            notificationService.subscribe(UUID.fromString(relativeId));
//        } else {
//            notificationService.unsubscribe(UUID.fromString(relativeId));
//        }
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/relatives/get-subscriptions-status")
//    // todo
//    public ResponseEntity<Boolean> getSubscriptionStatus(@RequestHeader("soul-token") String token) {
//        return ResponseEntity.ok(notificationService.getSubscriptionStatus(UUID.fromString(relativeId)));
//    }
}
