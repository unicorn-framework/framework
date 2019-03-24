package org.unicorn.framework.oauth.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author xiebin
 */
@Data
public class UnicornUser extends User {
    private Long id;
    public UnicornUser(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {

        super(username, password, true, true, true, true, authorities);
        this.id=id;
    }

    public UnicornUser(Long id, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id=id;
    }



}
