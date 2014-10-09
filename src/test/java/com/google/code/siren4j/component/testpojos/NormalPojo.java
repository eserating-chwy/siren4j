package com.google.code.siren4j.component.testpojos;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class NormalPojo {
   
    private int id;
    private String name;
    private Date lastmodify;
    private Collection<String> refs;
    private Map<String, String> testMap;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getLastmodify() {
        return lastmodify;
    }
    public void setLastmodify(Date lastmodify) {
        this.lastmodify = lastmodify;
    }
    public Collection<String> getRefs() {
        return refs;
    }
    public void setRefs(Collection<String> refs) {
        this.refs = refs;
    }
    public Map<String, String> getTestMap() {
        return testMap;
    }
    public void setTestMap(Map<String, String> testMap) {
        this.testMap = testMap;
    }
    
    
    
    

}
