package org.unicorn.framework.base.base;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author  xiebin
 */
public class UnicornPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }




    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equalsIgnoreCase(charSequence.toString());
    }


}
