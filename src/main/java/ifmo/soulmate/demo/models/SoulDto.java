package ifmo.soulmate.demo.models;

import ifmo.soulmate.demo.entities.enums.SoulStatus;

public class SoulDto {
    private String id;
    private SoulStatus type;
    private String info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SoulStatus getType() {
        return type;
    }

    public void setType(SoulStatus type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public SoulDto() {
    }

    public SoulDto(String id, SoulStatus type, String info) {
        this.id = id;
        this.type = type;
        this.info = info;
    }
}
