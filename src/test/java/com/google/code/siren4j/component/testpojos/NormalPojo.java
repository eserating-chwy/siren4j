package com.google.code.siren4j.component.testpojos;

import java.util.Collection;
import java.util.Date;

public class NormalPojo {
   
    private int id;
    private String name;
    private Date lastmodify;
    private Collection<String> refs;
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
    
    

}
