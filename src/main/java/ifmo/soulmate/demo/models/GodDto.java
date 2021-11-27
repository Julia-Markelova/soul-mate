package ifmo.soulmate.demo.models;

public class GodDto {
    private String id;
    private String userId;
    private String name;

    public GodDto(String id, String userId, String name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
    }

    public GodDto() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
