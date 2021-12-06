package ifmo.soulmate.demo.controllers;

import ifmo.soulmate.demo.entities.LifeTicket;
import ifmo.soulmate.demo.entities.enums.HelpRequestType;
import ifmo.soulmate.demo.entities.enums.SoulStatus;
import ifmo.soulmate.demo.entities.enums.UserRole;
import ifmo.soulmate.demo.exceptions.AuthException;
import ifmo.soulmate.demo.exceptions.MainApiException;
import ifmo.soulmate.demo.exceptions.NonExistingEntityException;
import ifmo.soulmate.demo.models.*;
import ifmo.soulmate.demo.services.LifeTicketService;
import ifmo.soulmate.demo.services.LoginService;
import ifmo.soulmate.demo.services.PersonalProgramService;
import ifmo.soulmate.demo.services.SoulService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class SoulController {

    @Autowired
    SoulService soulService;

    @Autowired
    LifeTicketService lifeTicketService;

    @Autowired
    LoginService loginService;

    @Autowired
    PersonalProgramService personalProgramService;

    private final int numberOfExercisesInPersonalProgram = 5;

    @GetMapping("/souls/profile")
    @ApiOperation(value = "Получить информацию о душе",
            notes = "Для запроса нужно быть авторизованной душой",
            response = ResponseEntity.class)
    public ResponseEntity<SoulDto> getSoulProfile(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            return ResponseEntity.ok(soulService.getSoulByUserId(UUID.fromString(userDto.getId())));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/souls/lives")
    @ApiOperation(value = "Получить информацию о жизнях, которые проживала/проживает душа",
            notes = "Для запроса нужно быть авторизованной душой",
            response = ResponseEntity.class)
    public ResponseEntity<List<LifeDto>> getSoulLives(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(soulService.getSoulLives(UUID.fromString(userDto.getRoleId())));
    }

    @GetMapping("/souls/life-tickets")
    public ResponseEntity<LifeTicket> getLifeTicket(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (NonExistingEntityException | AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(lifeTicketService.getNotUsedLifeTicket(UUID.fromString(userDto.getRoleId())));
    }

    @PutMapping("/souls/receive-life-ticket/{ticketId}")
    @ApiOperation(value = "Использовать билет в жизнь. Доступно только для душ UNBORN. Проставляет статус BORN",
            notes = "Для запроса нужно быть авторизованной душой. Создает для нерожденной души новую жизнь",
            response = ResponseEntity.class)
    public ResponseEntity<SoulDto> receiveLifeTicket(@RequestHeader("soul-token") String token, @PathVariable String ticketId) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (NonExistingEntityException | AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        try {
            lifeTicketService.receiveLifeTicket(UUID.fromString(userDto.getRoleId()), UUID.fromString(ticketId));
            return ResponseEntity.ok(soulService.getSoulByUserId(UUID.fromString(userDto.getRoleId())));
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
    }

    @PutMapping("/souls/update/status/{status}")
    public ResponseEntity<SoulDto> updateSoulStatus(@RequestHeader("soul-token") String token, @PathVariable String status) {
        UserDto userDto;
        SoulDto soulDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
            soulDto = soulService.updateSoulStatus(UUID.fromString(userDto.getRoleId()), SoulStatus.valueOf(status));
        } catch (NonExistingEntityException | AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(soulDto);
    }

    @PutMapping("/souls/update/mentor/{isMentor}")
    public ResponseEntity<SoulDto> updateSoulMentor(@RequestHeader("soul-token") String token, @PathVariable Boolean isMentor) {
        UserDto userDto;
        SoulDto soulDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Arrays.asList(UserRole.SOUL, UserRole.MENTOR));
            soulDto = soulService.updateSoulMentor(UUID.fromString(userDto.getRoleId()), isMentor);
        } catch (NonExistingEntityException | AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(soulDto);
    }

    @PostMapping("/souls/create-help-request/astral")
    @ApiOperation(value = "Создает заявку на выход из астрала",
            notes = "Для запроса нужно быть авторизованной душой",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> createHelpRequestAstral(@RequestHeader("soul-token") String token) throws NonExistingEntityException {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        HelpRequestDto helpRequestDto;
        try {
            helpRequestDto = soulService.createNewHelpRequestForGod(UUID.fromString(userDto.getRoleId()));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(helpRequestDto);
    }

    @GetMapping("/souls/my-requests/astral")
    @ApiOperation(value = "Получить список запросов на выход из астрала, которые созданы душой",
            notes = "Для запроса нужно быть авторизованной душой",
            response = ResponseEntity.class)
    public ResponseEntity<List<HelpRequestDto>> getGodRequests(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(soulService.getHelpRequestsBySoulId(UUID.fromString(userDto.getRoleId()), HelpRequestType.GOD));
    }

    @GetMapping("/souls/personal-program")
    @ApiOperation(value = "Получить информацию о персональной программе. " +
            "Если программы еще нет, то ошибка 404",
            notes = "Для запроса нужно быть авторизованной душой со статусом UNBORN. Количество упражнений в каждой программе фиксированно." +
                    "Задается в контроллеле души. По умолчанию 5.",
            response = ResponseEntity.class)
    public ResponseEntity<PersonalProgramDto> getPersonalProgram(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        SoulDto soulDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
            soulDto = soulService.getSoulByUserId(UUID.fromString(userDto.getId()));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        if (soulDto.getStatus() != SoulStatus.UNBORN) {
            return new ResponseEntity(String.format("Expected soul with status UNBORN but got %s", soulDto.getStatus()), HttpStatus.BAD_REQUEST);
        }
        try {
            return ResponseEntity.ok(personalProgramService.getPersonalProgramBySoulId(UUID.fromString(userDto.getRoleId())));
        } catch (NonExistingEntityException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/souls/personal-program")
    @ApiOperation(value = "Создать персональную программу. " +
            "Если программа уже есть, то ошибка 400",
            notes = "Для запроса нужно быть авторизованной душой со статусом UNBORN. Количество упражнений в каждой программе фиксированно." +
                    "Задается в контроллеле души. По умолчанию 5.",
            response = ResponseEntity.class)
    public ResponseEntity<PersonalProgramDto> createPersonalProgram(@RequestHeader("soul-token") String token,
                                                                    @RequestParam(defaultValue = "5") Integer exercisesCount) {
        UserDto userDto;
        SoulDto soulDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
            soulDto = soulService.getSoulByUserId(UUID.fromString(userDto.getId()));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        if (soulDto.getStatus() != SoulStatus.UNBORN) {
            return new ResponseEntity(String.format("Expected soul with status UNBORN but got %s", soulDto.getStatus()), HttpStatus.BAD_REQUEST);
        }
        try {
            personalProgramService.getPersonalProgramBySoulId(UUID.fromString(userDto.getRoleId()));
            return new ResponseEntity(String.format("Soul %s already has personal program", soulDto.getId()), HttpStatus.BAD_REQUEST);

        } catch (NonExistingEntityException ignored) {

        }
        try {
            return ResponseEntity.ok(personalProgramService.createPersonalProgram(UUID.fromString(userDto.getRoleId()), exercisesCount));
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/souls/personal-program/update-exercise-progress/{exerciseId}")
    @ApiOperation(value = "Обновить прогресс по упражнению в персональной программе. ",
            notes = "Если программы нет - ошибка. Иначе - устанавливает прогресс по упражнению " +
                    "в переданное значение. Значение должно быть в диапазоне [0, 100]. " +
                    "Если после обновления прогресса все упражнения будут завершены, то у программы " +
                    "обновится статус на SUCCESS." +
                    "Для запроса нужно быть авторизованной душой",
            response = ResponseEntity.class)
    public ResponseEntity<PersonalProgramDto> updateExerciseProgress(@RequestHeader("soul-token") String token,
                                                                     @PathVariable String exerciseId,
                                                                     @RequestParam Integer progress) {
        try {
            UserDto userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
            PersonalProgramDto programDto = personalProgramService.getPersonalProgramBySoulId(UUID.fromString(userDto.getRoleId()));
            personalProgramService.updateProgramExerciseProgress(UUID.fromString(programDto.getId()), UUID.fromString(exerciseId), progress);
            return ResponseEntity.ok(personalProgramService.getPersonalProgramBySoulId(UUID.fromString(userDto.getRoleId())));
        } catch (NonExistingEntityException | AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/souls/create-help-request/life-spark")
    @ApiOperation(value = "Создает заявку на поиск искры жизни",
            notes = "Для запроса нужно быть авторизованной душой",
            response = ResponseEntity.class)
    public ResponseEntity<HelpRequestDto> createHelpRequestLifeSpark(@RequestHeader("soul-token") String token) throws NonExistingEntityException {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (AuthException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        HelpRequestDto helpRequestDto;
        try {
            helpRequestDto = soulService.createNewHelpRequestForMentor(UUID.fromString(userDto.getRoleId()));
        } catch (NonExistingEntityException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(helpRequestDto);
    }

    @GetMapping("/souls/my-requests/life-spark")
    @ApiOperation(value = "Получить список запросов на поиск искры жизни, которые созданы душой",
            notes = "Для запроса нужно быть авторизованной душой",
            response = ResponseEntity.class)
    public ResponseEntity<List<HelpRequestDto>> getLifeSparkRequests(@RequestHeader("soul-token") String token) {
        UserDto userDto;
        try {
            userDto = loginService.authoriseAndCheckPermission(token, Collections.singletonList(UserRole.SOUL));
        } catch (MainApiException ex) {
            return new ResponseEntity(ex.getMessage(), ex.getStatus());
        }
        return ResponseEntity.ok(soulService.getHelpRequestsBySoulId(UUID.fromString(userDto.getRoleId()), HelpRequestType.MENTOR));
    }


}
