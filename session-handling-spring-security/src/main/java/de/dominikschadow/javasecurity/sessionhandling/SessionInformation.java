package de.dominikschadow.javasecurity.sessionhandling;

import org.springframework.web.context.request.RequestContextHolder;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class SessionInformation {
    public String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
}
