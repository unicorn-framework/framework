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
    private static final long serialVersionUID = 1L;
    //用户ID
    private Long id;
    //是否新用户
    private Boolean newUser;

    public UnicornUser(Long id, Boolean newUser, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, true, true, true, true, authorities);
        this.id=id;
        this.newUser=newUser;
    }

    public UnicornUser(Long id, Boolean newUser,String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id=id;
        this.newUser=newUser;
    }



}
