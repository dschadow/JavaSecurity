package de.dominikschadow.fwm.user;

import org.apache.shiro.authc.AuthenticationException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@ManagedBean
@SessionScoped
public class UserController {
    private User user;
    @Inject
    private UserBean userBean;

    @ManagedProperty("#{loginController}")
    private LoginController loginController;

    public List<User> getUsers() {
        if (!loginController.isUserAdmin()) {
            throw new AuthenticationException("User not authorized");
        }

        return userBean.getAllUser();
    }

    public String saveUser() {
        if (!loginController.isUserAdmin()) {
            throw new AuthenticationException("User not authorized");
        }

        userBean.save(user);

        return "/admin/users";
    }

    public String goCreate() {
        this.user = new User();

        return "user";
    }

    public String goEdit(User user) {
        this.user = user;

        return "user";
    }

    public String deleteUser(int id) {
        if (!loginController.isUserAdmin()) {
            throw new AuthenticationException("User not authorized");
        }

        userBean.deleteUser(id);

        return "users";
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
