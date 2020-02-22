package org.unicorn.framework.base.base;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author xiebin
 */
public class UnicornPasswordEncoder implements PasswordEncoder {
    private static final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence charSequence) {
        return bcryptPasswordEncoder.encode(charSequence);
    }


    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return bcryptPasswordEncoder.matches(charSequence.toString(), s);
    }


}
