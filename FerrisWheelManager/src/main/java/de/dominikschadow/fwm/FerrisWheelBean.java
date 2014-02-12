package de.dominikschadow.fwm;

import de.dominikschadow.fwm.session.LoginBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@ManagedBean
@RequestScoped
public class FerrisWheelBean {
    @PersistenceContext(unitName = "fwm")
    private EntityManager em;

    @ManagedProperty("#{loginBean}")
    private LoginBean loginBean;

    public List<FerrisWheel> getFerrisWheels() {
        Query query = em.createQuery("from FerrisWheel w where w.user=:user");
        query.setParameter("user", loginBean.getCurrentUser());

        List<FerrisWheel> results = query.getResultList();

        return results;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
}
