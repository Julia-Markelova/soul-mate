package ifmo.soulmate.demo.models;

import ifmo.soulmate.demo.entities.enums.SoulStatus;

public class SoulDto {
    private String id;
    private SoulStatus status;
    private String info;

    public boolean isMentor() {
        return isMentor;
    }

    public void setMentor(boolean mentor) {
        isMentor = mentor;
    }

    private boolean isMentor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SoulStatus getStatus() {
        return status;
    }

    public void setStatus(SoulStatus status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public SoulDto() {
    }

    public SoulDto(String id, SoulStatus status, String info, boolean isMentor) {
        this.id = id;
        this.status = status;
        this.info = info;
        this.isMentor = isMentor;
    }
}
