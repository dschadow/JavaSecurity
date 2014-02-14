package de.dominikschadow.fwm.session;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "USERS")
public class User implements Serializable {
    public static enum Role {
        Manager, User
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Size(min = 4, max = 20, message = "Username is required - minimum 4, maximum 20 characters")
    private String username;
    @NotNull
    @Size(min = 10, max = 128, message = "Password is required - minimum 10, maximum 128 characters")
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    @NotNull
    private String salt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
