package de.dominikschadow.fwm.session;

import de.dominikschadow.fwm.FerrisWheel;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class FerrisWheelBean {
    @PersistenceContext(unitName = "fwm")
    private EntityManager em;

    public List<FerrisWheel> getFerrisWheelsForUser(User user) {
        Query query = em.createQuery("from FerrisWheel w where w.user=:user");
        query.setParameter("user", user);

        return query.getResultList();
    }

    public FerrisWheel getFerrisWheelById(int id) {
        return em.find(FerrisWheel.class, id);
    }

    public List<FerrisWheel> getAllFerrisWheels() {
        Query query = em.createQuery("from FerrisWheel w");

        return query.getResultList();
    }

    public void deleteFerrisWheel(int id) {
        FerrisWheel ferrisWheel = getFerrisWheelById(id);
        em.remove(ferrisWheel);
    }

    public FerrisWheel save(FerrisWheel ferrisWheel) {
        if (ferrisWheel.getId() == null) {
            em.persist(ferrisWheel);
            return ferrisWheel;
        } else {
            return em.merge(ferrisWheel);
        }
    }
}
