package de.dominikschadow.javasecurity.sessionhandling.greetings;

/**
 *
 * @author Dominik Schadow
 */
public class GreetingServiceImpl implements GreetingService {
    @Override
    public String greetUser() {
        return "Hello user";
    }

    @Override
    public String greetAdmin() {
        return "Hello admin";
    }
}
