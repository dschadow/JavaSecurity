package de.dominikschadow.fwm.search;

import de.dominikschadow.fwm.FerrisWheel;
import de.dominikschadow.fwm.FerrisWheelBean;
import de.dominikschadow.fwm.user.LoginController;
import org.apache.shiro.authc.AuthenticationException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@RequestScoped
public class SearchController {

    @Inject
    private FerrisWheelBean ferrisWheelBean;

    @ManagedProperty("#{loginController}")
    private LoginController loginController;

    private Search search = new Search();
    private List<FerrisWheel> results = new ArrayList<>();

    public String search() {
        if (!loginController.isLoggedIn()) {
            throw new AuthenticationException("User not authenticated");
        }

        results = ferrisWheelBean.findFerrisWheels(search);

        return "search";
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public List<FerrisWheel> getResults() {
        return results;
    }
}
