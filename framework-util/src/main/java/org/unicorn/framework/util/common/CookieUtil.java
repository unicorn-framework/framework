/**
 * Title: CookieUtil.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2015<br/>
 * 
 *
 */
package org.unicorn.framework.util.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/**
 * Title: CookieUtil<br/>
 * Description: <br/>
 * 
 * @author xiebin
 *
 */
/** 
 * 这个方法用于重新解释cookie，并且实现secure及httponly等cookie属性。。 
 * 基本的cookie字符串格式： 
 * key=value; Expires=date;  Domain=domain; Path=path;Secure; HttpOnly 
 *         Set-Cookie: <name>=<value>[; <name>=<value>]... 
 [; expires=<date>][; domain=<domain_name>] 
 [; path=<some_path>][; secure][; httponly] 
 * */  
public class CookieUtil {  
  
    /** 
     * 添加cookie，当然，这个其实是将cookie的内容转换成为字符串，然后添加到header。 
     * 例如： 
     * response.addHeader("Set-Cookie",  "__wsidd=hhghgh ;Domain=localhost; Path=/; Max-Age=36000; Secure; HTTPOnly;"); 
     * 例子二： 
     * Set-Cookie：customer=huangxp; path=/foo; domain=.ibm.com; 
     * expires= Wednesday, 19-OCT-05 23:12:40 GMT; [secure] 
     * 注意，expires已经被Max-Age所代替。 
     * */  
    public static void addCookie(HttpServletResponse response,Cookie _cookie){  
        if(response==null||_cookie==null){  
            return;  
        }  
        response.addCookie(_cookie);
    }
    
    
    
    private static StringBuilder getCookieStr(Cookie _cookie){  
  
        StringBuilder sb=new StringBuilder();  
        sb.append(_cookie.getName());  
        sb.append('=');  
        if(!StringUtils.isEmpty(_cookie.getValue())){  
            sb.append(_cookie.getValue().trim());  
        }  
  
        //--max age属性或者expires属性，两者只能选择其中一个  
        if(_cookie.getMaxAge()>0){  
           
          
            sb.append("; max-age=");  
            sb.append(_cookie.getMaxAge());  
        }else{
            sb.append("; max-age=-1");  
           
        }  
        //--domain字符串  
        if(!StringUtils.isEmpty(_cookie.getDomain())){  
            sb.append("; domain=");  
            sb.append(_cookie.getDomain().trim());  
        }  
        //--构造path字符串  
        if(!StringUtils.isEmpty(_cookie.getPath())){  
            sb.append("; path=");  
            sb.append(_cookie.getPath().trim());  
        }  
        //--构造secure属性  
        if(_cookie.getSecure()){  
            sb.append("; Secure");  
        }  
        //--构造httponly属性  
        sb.append("; HttpOnly");  
         
        return sb;  
    }  
}  