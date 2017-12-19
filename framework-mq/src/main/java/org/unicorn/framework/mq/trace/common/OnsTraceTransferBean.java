package org.unicorn.framework.mq.trace.common;

import java.util.HashSet;
import java.util.Set;


/**
 * @author xiebin
 */
public class OnsTraceTransferBean {
    private String transData;
    private Set<String> transKey = new HashSet<String>();


    public String getTransData() {
        return transData;
    }


    public void setTransData(String transData) {
        this.transData = transData;
    }


    public Set<String> getTransKey() {
        return transKey;
    }


    public void setTransKey(Set<String> transKey) {
        this.transKey = transKey;
    }
}
