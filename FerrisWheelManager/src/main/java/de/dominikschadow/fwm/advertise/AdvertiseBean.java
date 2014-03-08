package de.dominikschadow.fwm.advertise;

import de.dominikschadow.fwm.FerrisWheel;
import de.dominikschadow.fwm.FerrisWheelBean;
import de.dominikschadow.fwm.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Stateless
public class AdvertiseBean {
    @PersistenceContext(unitName = "fwm")
    private EntityManager em;
    @Inject
    private FerrisWheelBean ferrisWheelBean;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void advertise(int id, User user) {
        logger.warn("Advertising for ferris wheel {}", id);
        FerrisWheel ferrisWheel = ferrisWheelBean.getFerrisWheelById(id);

        Advertise advertise = new Advertise();
        advertise.setFerrisWheel(ferrisWheel);
        advertise.setUser(user);
        advertise.setAdvertiseDate(new Date());

        em.persist(advertise);
    }
}
