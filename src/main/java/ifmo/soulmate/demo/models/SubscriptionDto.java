package ifmo.soulmate.demo.models;

import ifmo.soulmate.demo.entities.enums.SoulStatus;

public class SubscriptionDto {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;
    String relativeId;
    String soulId;
    SoulStatus soulStatus;
    boolean soulIsMentor;
    boolean isSubscribed;

    public SubscriptionDto(String id, String relativeId, String soulId, SoulStatus soulStatus, boolean soulIsMentor, boolean isSubscribed) {
        this.id = id;
        this.relativeId = relativeId;
        this.soulId = soulId;
        this.soulStatus = soulStatus;
        this.soulIsMentor = soulIsMentor;
        this.isSubscribed = isSubscribed;
    }

    public SubscriptionDto() {
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
