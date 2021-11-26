package ifmo.soulmate.demo.models;

import ifmo.soulmate.demo.entities.enums.SystemModeType;

public class SystemModeDto {
    private String id;
    private SystemModeType type;
    private Boolean isManualMode;

    public SystemModeDto() {

    }

    public SystemModeDto(String id, SystemModeType type, Boolean isManualMode) {
        this.id = id;
        this.type = type;
        this.isManualMode = isManualMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SystemModeType getType() {
        return type;
    }

    public void setType(SystemModeType type) {
        this.type = type;
    }

    public Boolean getManualMode() {
        return isManualMode;
    }

    public void setManualMode(Boolean manualMode) {
        isManualMode = manualMode;
    }
}
