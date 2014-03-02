package de.dominikschadow.fwm;

import de.dominikschadow.fwm.user.User;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "FERRIS_WHEELS")
public class FerrisWheel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Size(min = 3, max = 50, message = "Name is required - minimum 3, maximum 50 characters")
    private String name;
    @Size(max = 1024)
    private String description;
    @NotNull
    @Size(min =3 , max = 50, message = "Location is required - minimum 3, maximum 50 characters")
    private String location;
    @Min(0)
    @Max(1000)
    private int speed;
    @Column(name = "installation_date")
    @Temporal(TemporalType.DATE)
    private Date installationDate;
    @Column(name = "maintenance_date")
    @Temporal(TemporalType.DATE)
    @Future
    private Date maintenanceDate;
    @ManyToOne
    private User user;
    private Boolean online;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Date getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }

    public Date getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(Date maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
