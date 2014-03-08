package de.dominikschadow.fwm.advertise;

import de.dominikschadow.fwm.FerrisWheel;
import de.dominikschadow.fwm.FerrisWheelBean;
import de.dominikschadow.fwm.user.LoginController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.List;

@ManagedBean
@RequestScoped
public class AdvertiseController {
    @Inject
    private AdvertiseBean advertiseBean;
    @Inject
    private FerrisWheelBean ferrisWheelBean;

    private int ferrisWheelId;

    @ManagedProperty("#{loginController}")
    private LoginController loginController;

    public String advertise() {
        advertiseBean.advertise(ferrisWheelId, loginController.getCurrentUser());

        return "index";
    }

    public int getFerrisWheelId() {
        return ferrisWheelId;
    }

    public void setFerrisWheelId(int ferrisWheelId) {
        this.ferrisWheelId = ferrisWheelId;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public FerrisWheel[] getWheels() {
        List<FerrisWheel> ferrisWheels = ferrisWheelBean.getAllFerrisWheels();

        return ferrisWheels.toArray(new FerrisWheel[ferrisWheels.size()]);
    }
}
