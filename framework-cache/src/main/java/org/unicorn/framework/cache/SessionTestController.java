
package org.unicorn.framework.cache;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unicorn.framework.cache.annoation.SessionCheck;
import org.unicorn.framework.core.constants.Constants;
import org.unicorn.framework.core.exception.PendingException;

/**
*
*@author xiebin
*
*/
@RestController
@RequestMapping("/redis")
public class SessionTestController {
	
	@RequestMapping("/uid/{userName}")
    String set(HttpSession session,@PathVariable("userName") String userName)throws PendingException {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        session.setAttribute("userName", userName);
        session.setAttribute(Constants.SESSION_FLAG, true);
        System.out.println(session.getAttribute("userName"));
        return session.getId();
    }
	
	@RequestMapping("/get")
	@SessionCheck
    String get(HttpSession session) throws PendingException{
        System.out.println(session.getAttribute("userName"));
        return session.getId();
    }
}

