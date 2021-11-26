package ifmo.soulmate.demo.services;

import ifmo.soulmate.demo.controllers.LoginController;
import ifmo.soulmate.demo.entities.SystemMode;
import ifmo.soulmate.demo.models.SystemModeDto;
import ifmo.soulmate.demo.repositories.AdminRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    private static final Logger log = LogManager.getLogger(LoginController.class);

    public List<SystemModeDto> getAllModes() {
        log.info("Get all modes");
        List<SystemMode> modes = adminRepository.findAll();
        return modes
                .stream()
                .map(x -> {
                    try {
                        return new SystemModeDto(x.getId().toString(), x.getType(), x.getManualMode());
                    } catch (Exception e) {
                        log.warn("Error in getting all modes: {}", e.toString());
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void updateMode(UUID modeId, Boolean isManualMode) {
        Optional<SystemMode> mode = adminRepository.findById(modeId);
        if (mode.isPresent()) {
            SystemMode unwrapped = mode.get();
            if (unwrapped.getManualMode() != isManualMode) {
//                todo: остановить/запустить автоматический режим чего-либо
                unwrapped.setManualMode(isManualMode);
                adminRepository.saveAndFlush(unwrapped);
            }
        }
    }
}
