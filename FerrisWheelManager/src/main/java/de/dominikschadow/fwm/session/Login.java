package de.dominikschadow.fwm.session;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@ManagedBean
@SessionScoped
public class Login {
    Credentials credentials;

    @PersistenceContext
    EntityManager userDb;

    private User user;

    public String login() {
        List<User> results = userDb.createQuery(
                "select u from User u where u.username=:username and u.password=:password", User.class)
                .setParameter("username", credentials.getUsername())
                .setParameter("password", credentials.getPassword())
                .getResultList();

        if (!results.isEmpty()) {
            user = results.get(0);
        }

        return "index";
    }

    public void logout() {
        user = null;
    }

    public User getCurrentUser() {
        return user;
    }

    public boolean isLoggedIn() {
        return user != null;
    }
}
