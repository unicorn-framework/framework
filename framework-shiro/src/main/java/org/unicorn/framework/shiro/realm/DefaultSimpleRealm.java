package org.unicorn.framework.shiro.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
/**
 * 
 * @author xiebin
 *
 */
public class DefaultSimpleRealm extends AuthorizingRealm {

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println("用户名====" + arg0.getPrincipal());
		System.out.println("密码====" + new String((char[]) arg0.getCredentials()));
		String userName = (String) arg0.getPrincipal();
		String accountPassword ="c0a1a2cb1bd77cbb48cdddfd5533422b"; //模拟库中的密码
		SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userName, accountPassword,ByteSource.Util.bytes("123"),
				getName());
		return simpleAuthenticationInfo;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		// TODO Auto-generated method stub
		
		 String username = (String) principals.getPrimaryPrincipal();
		 System.out.println("用户名===="+username);
	        // 从数据库或者缓存中获得角色数据
	        Set<String> roles = new HashSet<>();
	        Set<String> permissions = new HashSet<>();
	        roles.add("admin");
	        //上面的service层方法需要自己写
	        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
	        simpleAuthorizationInfo.setStringPermissions(permissions);
	        simpleAuthorizationInfo.setRoles(roles);

	        return simpleAuthorizationInfo;
	}

}
