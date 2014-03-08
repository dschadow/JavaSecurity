package de.dominikschadow.fwm.user;

import org.apache.shiro.authc.AuthenticationException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@ManagedBean
@RequestScoped
public class UserController {
    private User user = new User();
    @Inject
    private UserBean userBean;

    @ManagedProperty("#{loginController}")
    private LoginController loginController;

    public String createUser() {
        if (!loginController.isUserAdmin()) {
            throw new AuthenticationException("User not authorized");
        }

        userBean.createNewUser(user);

        return "/users/index";
    }

    public List<User.Role> getRoles() {
        return Arrays.asList(User.Role.values());
    }

    public List<User.Unit> getUnits() {
        return Arrays.asList(User.Unit.values());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
