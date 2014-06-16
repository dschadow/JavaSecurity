package de.dominikschadow.javasecurity.sessionhandling.greetings;

import org.springframework.stereotype.Service;

/**
 *
 * @author Dominik Schadow
 */
@Service
public class GreetingServiceImpl implements GreetingService {
    @Override
    public String greetUser() {
        return "Spring bean says hello to the user!";
    }

    @Override
    public String greetAdmin() {
        return "Spring bean says hello to the admin!";
    }
}
