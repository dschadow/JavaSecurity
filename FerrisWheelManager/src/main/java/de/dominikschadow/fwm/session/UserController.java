package de.dominikschadow.fwm.session;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ManagedBean
@RequestScoped
public class UserController {
    private User user = new User();

    @PersistenceContext(unitName = "fwm")
    private EntityManager em;

    @Transactional(Transactional.TxType.REQUIRED)
    public String createUser() {
        em.persist(user);

        return "/users/index";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
