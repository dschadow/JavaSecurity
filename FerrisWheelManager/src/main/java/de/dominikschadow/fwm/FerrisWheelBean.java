package de.dominikschadow.fwm;

import de.dominikschadow.fwm.search.Search;
import de.dominikschadow.fwm.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class FerrisWheelBean {
    @PersistenceContext(unitName = "fwm")
    private EntityManager em;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public List<FerrisWheel> getFerrisWheelsForUser(User user) {
        Query query = em.createQuery("from FerrisWheel w where w.user=:user", FerrisWheel.class);
        query.setParameter("user", user);

        return query.getResultList();
    }

    public FerrisWheel getFerrisWheelById(int id) {
        return em.find(FerrisWheel.class, id);
    }

    public List<FerrisWheel> getAllFerrisWheels() {
        Query query = em.createQuery("from FerrisWheel w", FerrisWheel.class);

        return query.getResultList();
    }

    public void deleteFerrisWheel(int id) {
        FerrisWheel ferrisWheel = getFerrisWheelById(id);
        em.remove(ferrisWheel);
    }

    public FerrisWheel save(FerrisWheel ferrisWheel, User user) {
        if (ferrisWheel.getId() == null) {
            ferrisWheel.setOnline(false);
            ferrisWheel.setUser(user);

            em.persist(ferrisWheel);
            return ferrisWheel;
        } else {
            return em.merge(ferrisWheel);
        }
    }

    public List<FerrisWheel> findFerrisWheels(Search search) {
        Query query = em.createNativeQuery("select * from ferris_wheels where name = '" + search.getName() + "'", FerrisWheel.class);
        List resultList = query.getResultList();

        logger.info("Search returned {} results", resultList.size());

        return resultList;
    }
}
