package de.dominikschadow.javasecurity.csrf.spring;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Activates the Spring Security configuration.
 *
 * @author Dominik Schadow
 */
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {
    public SecurityWebApplicationInitializer() {
        super(SecurityConfig.class);
    }
}
