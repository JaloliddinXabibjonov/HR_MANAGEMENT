package uz.pdp.hr_management1.service.authority;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.pdp.hr_management1.entity.User;

import java.util.Collection;

@Data
@AllArgsConstructor
public class Authority {
    public String getAuthority() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.isAuthenticated() &&
                !authentication.getPrincipal().equals("anonymousUser")) {
            Collection<? extends GrantedAuthority> authorities = ((User) authentication.getPrincipal()).getAuthorities();
            for (GrantedAuthority authority : authorities) {
                String authority1 = authority.getAuthority();
                return authority1;
            }

        }
        return null;
    }

    public User getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
