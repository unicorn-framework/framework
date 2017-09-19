package org.framework.redis;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.unicorn.framework.cache.Application;
import org.unicorn.framework.cache.cache.CacheService;
import org.unicorn.framework.cache.vo.User;

import com.google.gson.Gson;

import junit.framework.Assert;

/**
 * 
 * @author xiebin
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@SpringBootTest(classes = Application.class)  
public class TestRedis

{

	@Autowired
	private CacheService cacheService;

	@Test
	public void test() throws Exception {
//		stringRedisTemplate.opsForValue().set("aaa", "111");
		
		cacheService.put("aaa", "1111",1,TimeUnit.SECONDS, "test");
		
		Assert.assertEquals("1111", cacheService.get("aaa","test"));
	}

	@Test
	public void testObj() throws Exception {
		User user =User.builder().name("zhangsan").sex("男").build();
		
		cacheService.put("org.unicorn.user", user, 5, TimeUnit.SECONDS, "test");
		
		Thread.sleep(2000);
		boolean exists = cacheService.exists("org.unicorn.user","test");
		if (exists) {
			System.out.println("exists is true=====>"+new Gson().toJson(cacheService.get("org.unicorn.user","test")));
		} else {
			System.out.println("exists is false");
		}
	}
}
