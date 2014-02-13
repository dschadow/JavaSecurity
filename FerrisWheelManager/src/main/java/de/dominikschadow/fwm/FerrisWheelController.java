package de.dominikschadow.fwm;

import de.dominikschadow.fwm.session.FerrisWheelBean;
import de.dominikschadow.fwm.session.LoginController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.List;

@ManagedBean
@SessionScoped
public class FerrisWheelController {
    @Inject
    private FerrisWheelBean ferrisWheelBean;

    private FerrisWheel ferrisWheel;

    @ManagedProperty("#{loginController}")
    private LoginController loginController;

    public List<FerrisWheel> getFerrisWheels() {
        if (loginController.isUserAdmin()) {
            return ferrisWheelBean.getAllFerrisWheels();
        } else {
            return ferrisWheelBean.getFerrisWheelsForUser(loginController.getCurrentUser());
        }
    }

    public String saveFerrisWheel() {
        ferrisWheelBean.save(ferrisWheel);

        return "/users/index";
    }

    public String goEdit(FerrisWheel ferrisWheel) {
        this.ferrisWheel = ferrisWheel;

        return "/users/ferriswheel";
    }

    public String goCreate() {
        this.ferrisWheel = new FerrisWheel();

        return "/users/ferriswheel";
    }

    public String deleteFerrisWheel(int id) {
        ferrisWheelBean.deleteFerrisWheel(id);

        return "/users/index";
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public FerrisWheel getFerrisWheel() {
        return ferrisWheel;
    }

    public void setFerrisWheel(FerrisWheel ferrisWheel) {
        this.ferrisWheel = ferrisWheel;
    }
}
