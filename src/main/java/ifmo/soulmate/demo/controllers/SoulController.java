package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.models.Soul;
import ifmo.soulmate.demo.repositories.SoulRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class SoulController {

    @Autowired
    SoulRepository soulRepository;

    @GetMapping("/souls")
    public ResponseEntity<List<Soul>> getAllTutorials(@RequestParam(required = false) String title) {
        return ResponseEntity.ok(soulRepository.findAll());
    }
}
