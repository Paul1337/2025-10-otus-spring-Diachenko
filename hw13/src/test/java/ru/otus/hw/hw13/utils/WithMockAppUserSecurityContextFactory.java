package ru.otus.hw.hw13.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.otus.hw.hw13.services.security.AppUserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockAppUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockAppUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockAppUser annotation) {
        var context = SecurityContextHolder.createEmptyContext();

        var authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + annotation.role())
        );

        var principal = new AppUserDetails(
                annotation.id(),
                annotation.username(),
                "password",
                authorities
        );

        var auth = new UsernamePasswordAuthenticationToken(
                principal,
                principal.getPassword(),
                principal.getAuthorities()
        );

        context.setAuthentication(auth);
        return context;
    }
}