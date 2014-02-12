package de.dominikschadow.fwm;

import de.dominikschadow.fwm.session.LoginController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@ManagedBean
@RequestScoped
public class FerrisWheelController {
    @PersistenceContext(unitName = "fwm")
    private EntityManager em;

    @ManagedProperty("#{loginController}")
    private LoginController loginController;

    public List<FerrisWheel> getFerrisWheels() {
        Query query;

        if (loginController.isUserAdmin()) {
            query = em.createQuery("from FerrisWheel w");
        } else {
            query = em.createQuery("from FerrisWheel w where w.user=:user");
            query.setParameter("user", loginController.getCurrentUser());
        }

        List<FerrisWheel> results = query.getResultList();

        return results;
    }

    public String createFerrisWheel() {
        return "/users/index";
    }

    public void deleteFerrisWheel() {

    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
