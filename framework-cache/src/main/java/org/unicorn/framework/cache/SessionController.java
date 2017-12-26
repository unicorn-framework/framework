
package org.unicorn.framework.cache;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
*
*@author xiebin
*
*/
@RestController
@RequestMapping("/redis")
public class SessionController {
	@RequestMapping("/uid")
    String set(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        session.setAttribute("userName", "zs");
        System.out.println(session.getAttribute("userName"));
        return session.getId();
    }
	
	@RequestMapping("/get")
    String get(HttpSession session) {
        System.out.println(session.getAttribute("userName"));
        return session.getId();
    }
}

