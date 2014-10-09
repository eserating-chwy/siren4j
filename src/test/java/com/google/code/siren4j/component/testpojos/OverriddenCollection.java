package com.google.code.siren4j.component.testpojos;

import com.google.code.siren4j.resource.CollectionResource;

public class OverriddenCollection extends CollectionResource<OverriddenCollection> {
    
    private String dummy;

    public String getDummy() {
        return dummy;
    }

    public void setDummy(String dummy) {
        this.dummy = dummy;
    }
    
    

}
