package de.dominikschadow.fwm;

import de.dominikschadow.fwm.user.LoginController;
import org.apache.shiro.authc.AuthenticationException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.util.Arrays;
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
        if (!loginController.isLoggedIn()) {
            throw new AuthenticationException("User not authenticated");
        }

        List<FerrisWheel> ferrisWheels;

        if (loginController.isUserAdmin()) {
            ferrisWheels = ferrisWheelBean.getAllFerrisWheels();
        } else {
            ferrisWheels = ferrisWheelBean.getFerrisWheelsForUser(loginController.getCurrentUser());
        }

        return ferrisWheels;
    }

    public String saveFerrisWheel() {
        ferrisWheelBean.save(ferrisWheel, loginController.getCurrentUser());

        return "index";
    }

    public String goEdit(FerrisWheel ferrisWheel) {
        this.ferrisWheel = ferrisWheel;

        return "ferriswheel";
    }

    public String goCreate() {
        this.ferrisWheel = new FerrisWheel();

        return "ferriswheel";
    }

    public String deleteFerrisWheel(int id) {
        ferrisWheelBean.deleteFerrisWheel(id);

        return "index";
    }

    public List<Integer> getSpeed() {
        return Arrays.asList(25, 50, 75, 100, 125, 150);
    }

    public String turnFerrisWheelOn(FerrisWheel ferrisWheel) {
        ferrisWheel.setOnline(true);
        ferrisWheelBean.save(ferrisWheel, loginController.getCurrentUser());

        return "index";
    }

    public String turnFerrisWheelOff(FerrisWheel ferrisWheel) {
        ferrisWheel.setOnline(false);
        ferrisWheelBean.save(ferrisWheel, loginController.getCurrentUser());

        return "index";
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
