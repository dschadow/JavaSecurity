package de.dominikschadow.fwm;

import de.dominikschadow.fwm.user.LoginController;

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
    private FerrisWheel[] wheels = new FerrisWheel[0];

    @ManagedProperty("#{loginController}")
    private LoginController loginController;

    public List<FerrisWheel> getFerrisWheels() {
        List<FerrisWheel> ferrisWheels;

        if (loginController.isUserAdmin()) {
            ferrisWheels = ferrisWheelBean.getAllFerrisWheels();
        } else {
            ferrisWheels = ferrisWheelBean.getFerrisWheelsForUser(loginController.getCurrentUser());
        }
        
        updateAdvertisement(ferrisWheels);

        return ferrisWheels;
    }

    private void updateAdvertisement(List<FerrisWheel> ferrisWheels) {
        wheels = new FerrisWheel[ferrisWheels.size()];
        int i = 0;

        for (FerrisWheel ferrisWheel : ferrisWheels) {
            wheels[i] = ferrisWheel;
            i++;
        }
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

    public FerrisWheel[] getWheels() {
        return wheels;
    }
}
