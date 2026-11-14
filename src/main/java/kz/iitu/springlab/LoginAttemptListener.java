package kz.iitu.springlab;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LoginAttemptListener {

    private static final int MAX_ATTEMPTS = 5;

    private final AppUserRepository users;

    public LoginAttemptListener(AppUserRepository users) {
        this.users = users;
    }

    @EventListener
    @Transactional
    public void onFailure(AuthenticationFailureBadCredentialsEvent event) {
        String username = event.getAuthentication().getName();
        users.findByUsername(username).ifPresent(u -> {
            if (u.isLocked()) {
                return;
            }
            u.setFailedAttempts(u.getFailedAttempts() + 1);
            if (u.getFailedAttempts() >= MAX_ATTEMPTS) {
                u.setLocked(true);
            }
            users.save(u);
        });
    }

    @EventListener
    @Transactional
    public void onSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        users.findByUsername(username).ifPresent(u -> {
            if (u.getFailedAttempts() != 0) {
                u.setFailedAttempts(0);
                users.save(u);
            }
        });
    }
}
