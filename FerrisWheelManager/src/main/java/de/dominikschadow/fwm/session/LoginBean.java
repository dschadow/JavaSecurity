package de.dominikschadow.fwm.session;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@ManagedBean
@SessionScoped
public class LoginBean {
    @ManagedProperty("#{credentials}")
    private Credentials credentials;

//    @PersistenceContext(unitName = "fwm")
//    private EntityManager em;

    private User user;

    public String login() {
//        Query query = em.createQuery("select u from User u where u.username=:username and u.password=:password");
//        query.setParameter("username", credentials.getUsername());
//        query.setParameter("password", credentials.getPassword());
//
//        user = (User) query.getSingleResult();

        if (user == null) { // !=
            return "index";
        }

        return "login";
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

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
