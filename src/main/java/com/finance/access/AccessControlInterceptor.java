package com.finance.access;

import com.finance.exception.AccessDeniedException;
import com.finance.exception.InactiveUserException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.model.Role;
import com.finance.model.User;
import com.finance.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class AccessControlInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    // Constructor injection (preferred over @Autowired)
    public AccessControlInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // If it's not a controller method, skip (e.g., static resources)
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // Check if this method has @RequiresRole annotation
        RequiresRole requiresRole = handlerMethod.getMethodAnnotation(RequiresRole.class);

        // If no annotation, the endpoint is open (e.g., health check)
        if (requiresRole == null) {
            return true;
        }

        // Get the email from request header — this is how user identifies themselves
        String email = request.getHeader("X-User-Email");

        if (email == null || email.isBlank()) {
            throw new AccessDeniedException("Missing X-User-Email header");
        }

        // Look up the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // Block inactive users from everything
        if (!user.isActive()) {
            throw new InactiveUserException("Your account is inactive. Please contact an administrator.");
        }

        // Check if user's role is in the list of allowed roles
        Role[] allowedRoles = requiresRole.value();
        boolean hasPermission = Arrays.asList(allowedRoles).contains(user.getRole());

        if (!hasPermission) {
            throw new AccessDeniedException(
                "Role " + user.getRole() + " is not allowed to perform this action"
            );
        }

        // All checks passed — allow the request
        return true;
    }
}