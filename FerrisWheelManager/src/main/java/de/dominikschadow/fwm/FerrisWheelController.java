package de.dominikschadow.fwm;

import de.dominikschadow.fwm.session.FerrisWheelBean;
import de.dominikschadow.fwm.session.LoginController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@ManagedBean
@RequestScoped
public class FerrisWheelController {
    @Inject
    private FerrisWheelBean ferrisWheelBean;

    @ManagedProperty("#{loginController}")
    private LoginController loginController;

    public List<FerrisWheel> getFerrisWheels() {
        if (loginController.isUserAdmin()) {
            return ferrisWheelBean.getAllFerrisWheels();
        } else {
            return ferrisWheelBean.getFerrisWheelsForUser(loginController.getCurrentUser());
        }
    }

    public String createFerrisWheel() {
        return "/users/index";
    }

    public String editFerrisWheel(int id) {
        ferrisWheelBean.getFerrisWheelById(id);

        return "/users/ferriswheel";

    }

    public String deleteFerrisWheel(int id) {
        ferrisWheelBean.deleteFerrisWheel(id);

        return "/users/index";
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
