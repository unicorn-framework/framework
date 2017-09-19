package org.unicron.framework.mybatis.config;
/**
 * 
 * @author xiebin
 *
 */
public enum RoutingStrategy {
	 Master(true, "master"), Slave(false, "slave");

    private boolean write;
    private String key;

    RoutingStrategy(boolean write, String key) {
        this.write = write;
        this.key = key;
    }

    public boolean isWrite() {
        return write;
    }

    public String getKey() {
        return key;
    }

}
