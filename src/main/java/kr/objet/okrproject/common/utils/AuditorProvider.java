package kr.objet.okrproject.common.utils;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class AuditorProvider implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isEmpty(authentication) || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        authentication.getPrincipal();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return Optional.of(principal.getUsername());
    }
}


