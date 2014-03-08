package de.dominikschadow.fwm.user;

import org.apache.shiro.crypto.hash.Sha512Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class UserBean {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext(unitName = "fwm")
    private EntityManager em;

    public User save(User user) {
        String hash = hashPassword(user.getPassword(), user.getUsername());
        user.setPassword(hash);
        user.setSalt(user.getUsername());

        if (user.getId() == null) {
            logger.debug("Created a new user with password hash " + hash);

            em.persist(user);

            return user;
        } else {
            logger.debug("Updated user with password hash " + hash);

            return em.merge(user);
        }
    }

    // username is not a good salt, but OK for demo...
    public String hashPassword(String password, String salt) {
        Sha512Hash hash = new Sha512Hash(password, salt, 100000);

        return hash.toString();
    }

    public User getUser(String username, String password) {
        String hash = hashPassword(password, username);

        List<User> users = em.createQuery("from User u where u.username='" + username + "' and u.password='" + hash + "'", User.class).getResultList();

        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            logger.info("No user found");
            return null;
        }
    }

    public List<User> getAllUser() {
        Query query = em.createQuery("from User u", User.class);

        return query.getResultList();
    }

    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    public void deleteUser(int id) {
        User user = getUserById(id);
        em.remove(user);
    }
}
