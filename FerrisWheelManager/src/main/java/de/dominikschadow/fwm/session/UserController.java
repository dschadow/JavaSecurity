package de.dominikschadow.fwm.session;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

@ManagedBean
@RequestScoped
public class UserController {
    private User user = new User();
    @Inject
    private UserBean userBean;

    public String createUser() {
        userBean.createNewUser(user);

        return "/users/index";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
