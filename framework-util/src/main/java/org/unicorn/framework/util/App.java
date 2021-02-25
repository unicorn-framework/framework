package org.unicorn.framework.util;

import cn.hutool.core.util.IdUtil;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println(IdUtil.getSnowflake(10, 1).nextId());
    }
}
