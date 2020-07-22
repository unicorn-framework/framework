package org.unicorn.framework.web.base;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.unicorn.framework.base.constants.UnicornConstants;

/**
 * @author xiebin
 */
public class UnicornPasswordEncoder implements PasswordEncoder {
    private static final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();


    @Override
    public String encode(CharSequence charSequence) {
        //是app端不加密  权宜之计
        if (charSequence.toString().startsWith(UnicornConstants.APP_ECODE_PRE)) {
            return charSequence.toString();
        }
        return bcryptPasswordEncoder.encode(charSequence);
    }


    @Override
    public boolean matches(CharSequence charSequence, String encodeString) {
        if (encodeString.contains(UnicornConstants.APP_ECODE_PRE)) {
            return charSequence.equals(encodeString.replaceFirst(UnicornConstants.APP_ECODE_PRE, ""));
        }
        return bcryptPasswordEncoder.matches(charSequence.toString(), encodeString);
    }


}
