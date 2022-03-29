package uz.pdp.hr_management1.config;

import uz.pdp.hr_management1.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class SpringSecurityAudit implements AuditorAware<UUID> {
    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null &&
                authentication.isAuthenticated()&&
                !authentication.getPrincipal().equals("anonymousUser")){
            return Optional.of(((User)authentication.getPrincipal()).getId());
        }
        return Optional.empty();
    }
}
