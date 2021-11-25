package ifmo.soulmate.demo.models;

import ifmo.soulmate.demo.entities.UserRole;

public class UserDto {
    private String id;
    private UserRole role;
    private String login;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UserDto() {
    }

    public UserDto(String id, UserRole role, String login) {
        this.id = id;
        this.role = role;
        this.login = login;
    }
}
