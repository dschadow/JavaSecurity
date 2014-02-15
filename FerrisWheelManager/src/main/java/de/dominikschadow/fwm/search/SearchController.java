package de.dominikschadow.fwm.search;

import de.dominikschadow.fwm.FerrisWheel;
import de.dominikschadow.fwm.session.FerrisWheelBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@RequestScoped
public class SearchController {

    @Inject
    private FerrisWheelBean ferrisWheelBean;

    private Search search = new Search();
    private List<FerrisWheel> results = new ArrayList<>();

    public String search() {
        results = ferrisWheelBean.findFerrisWheels(search);

        return "search";
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public List<FerrisWheel> getResults() {
        return results;
    }
}
