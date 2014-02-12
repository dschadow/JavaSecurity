package de.dominikschadow.fwm.session;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@ManagedBean
@SessionScoped
public class LoginBean {
    @ManagedProperty("#{credentials}")
    private Credentials credentials;

    @PersistenceContext(unitName = "fwm")
    private EntityManager em;

    private User user;

    public String login() {
        Query query = em.createQuery("from User u where u.username=:username and u.password=:password");
        query.setParameter("username", credentials.getUsername());
        query.setParameter("password", credentials.getPassword());

        try {
            user = (User) query.getSingleResult();
        } catch (NoResultException ex) {
            addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username or password not correct", null));
            return "login";
        }

        return "/users/index";
    }

    private void addMessage(FacesMessage message){
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void logout() {
        user = null;
    }

    public User getCurrentUser() {
        return user;
    }

    public boolean isUserAdmin() {
        return "Admin".equals(user.getRole());
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
