package de.dominikschadow.fwm.advertise;

import de.dominikschadow.fwm.FerrisWheel;
import de.dominikschadow.fwm.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "fwm", name = "ADVERTISE")
public class Advertise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "advertise_date")
    @Temporal(TemporalType.DATE)
    private Date advertiseDate;
    @ManyToOne
    private User user;
    @ManyToOne
    private FerrisWheel ferrisWheel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getAdvertiseDate() {
        return advertiseDate;
    }

    public void setAdvertiseDate(Date advertiseDate) {
        this.advertiseDate = advertiseDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FerrisWheel getFerrisWheel() {
        return ferrisWheel;
    }

    public void setFerrisWheel(FerrisWheel ferrisWheel) {
        this.ferrisWheel = ferrisWheel;
    }
}
