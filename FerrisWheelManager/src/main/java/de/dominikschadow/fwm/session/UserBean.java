package de.dominikschadow.fwm.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UserBean {
    @PersistenceContext(unitName = "fwm")
    private EntityManager em;

    public void createNewUser(User user) {
        em.persist(user);
    }

    public User getUser(String username, String password) {
        Query query = em.createQuery("from User u where u.username=:username and u.password=:password");
        query.setParameter("username", username);
        query.setParameter("password", password);

        User user = null;

        try {
            user = (User) query.getSingleResult();
        } catch (NoResultException ex) {

        }

        return user;
    }
}
