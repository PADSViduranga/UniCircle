package com.example.UniCricle.Auth;

import com.example.UniCricle.Repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserRepository userRepository;

    private final int MAX_FAILED_ATTEMPTS = 3;
    private final int LOCK_TIME_DURATION = 3; // hours

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");

        // Start with the default message
        final String[] errorMessage = {"Invalid username or password."};

        if (username != null) {
            userRepository.findByUsername(username).ifPresent(user -> {
                // First, do your counting logic
                if (!user.isAccountLocked()) {
                    int newFailAttempts = user.getFailedAttempts() + 1;
                    user.setFailedAttempts(newFailAttempts);

                    if (newFailAttempts >= MAX_FAILED_ATTEMPTS) {
                        user.setAccountLocked(true);
                        user.setLockTime(LocalDateTime.now().plusHours(LOCK_TIME_DURATION));
                        errorMessage[0] = "Your account is locked " +
                                "please try again in three hours";
                    }
                    userRepository.save(user);
                } else {
                    // If the user is ALREADY locked in the DB, set the message immediately
                    errorMessage[0] = "Your account is locked" +
                            " please try again in three hours";
                }
            });
        }

        // Double check: if Spring officially sent a LockedException
        if (exception instanceof org.springframework.security.authentication.LockedException) {
            errorMessage[0] = "Your account is locked " +
                    " please try again in three hours";
        }

        // Force the session to update
        request.getSession().setAttribute("errorMessage", errorMessage[0]);

        super.setDefaultFailureUrl("/login?error=true");
        super.onAuthenticationFailure(request, response, exception);
    }
}
