package com.example.jw.aspect;

import com.example.jw.annotation.Authorized;
import com.example.jw.config.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Around("@annotation(authorized)")
    public Object authorize(ProceedingJoinPoint joinPoint, Authorized authorized) throws Throwable {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new AccessDeniedException("Missing or invalid Authorization header");
        }

        String token = header.substring(7);

        if (!tokenProvider.validateToken(token)) {
            throw new AccessDeniedException("Invalid or expired token");
        }

        String userScope = tokenProvider.getScopeFromToken(token);
        String requiredScope = authorized.scope();


        if (!userScope.equals(requiredScope) && !userScope.equals("ALL")) {
            throw new AccessDeniedException("Forbidden: Required scope " + requiredScope);
        }

        return joinPoint.proceed();
    }
}