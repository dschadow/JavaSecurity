package de.dominikschadow.fwm.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ManagedBean
@SessionScoped
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ManagedProperty("#{credentialsController}")
    private CredentialsController credentials;
    @Inject
    private UserBean userBean;

    private User user;

    public String login() {
        user = userBean.getUser(credentials.getUsername(), credentials.getPassword());

        if (user == null) {
            logger.warn("Username or password not correct");
            addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username or password not correct", null));
            return "login";
        }

        return "/users/index";
    }

    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void logout() {
        user = null;
    }

    public User getCurrentUser() {
        return user;
    }

    public boolean isUserAdmin() {
        return User.Role.Manager.equals(user.getRole());
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void setCredentials(CredentialsController credentials) {
        this.credentials = credentials;
    }
}
