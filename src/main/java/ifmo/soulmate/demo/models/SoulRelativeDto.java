package ifmo.soulmate.demo.models;

import ifmo.soulmate.demo.entities.enums.SoulStatus;

public class SoulRelativeDto {
    String relativeId;
    String soulId;
    SoulStatus soulStatus;
    boolean soulIsMentor;
    boolean isSubscribed;

    public SoulRelativeDto(String relativeId, String soulId, SoulStatus soulStatus,  boolean soulIsMentor, boolean isSubscribed) {
        this.relativeId = relativeId;
        this.soulId = soulId;
        this.soulStatus = soulStatus;
        this.soulIsMentor = soulIsMentor;
        this.isSubscribed = isSubscribed;
    }

    public SoulRelativeDto() {
    }

    public String getRelativeId() {
        return relativeId;
    }

    public void setRelativeId(String relativeId) {
        this.relativeId = relativeId;
    }

    public String getSoulId() {
        return soulId;
    }

    public void setSoulId(String soulId) {
        this.soulId = soulId;
    }

    public SoulStatus getSoulStatus() {
        return soulStatus;
    }

    public void setSoulStatus(SoulStatus soulStatus) {
        this.soulStatus = soulStatus;
    }

    public boolean isSoulIsMentor() {
        return soulIsMentor;
    }

    public void setSoulIsMentor(boolean soulIsMentor) {
        this.soulIsMentor = soulIsMentor;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
}
