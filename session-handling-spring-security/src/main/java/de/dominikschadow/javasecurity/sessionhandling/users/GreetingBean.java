package de.dominikschadow.javasecurity.sessionhandling.users;

import de.dominikschadow.javasecurity.sessionhandling.greetings.GreetingService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class GreetingBean {
    @ManagedProperty(value = "#{greetingServiceImpl}")
    private GreetingService greetingService;

    public String greetUser() {
        return greetingService.greetUser();
    }

    public String greetAdmin() {
        return greetingService.greetAdmin();
    }

    public void setGreetingService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }
}
