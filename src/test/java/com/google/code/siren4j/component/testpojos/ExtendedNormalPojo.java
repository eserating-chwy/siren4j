package com.google.code.siren4j.component.testpojos;

import java.util.Collection;

import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JSubEntity;

@Siren4JEntity(name = "extendedNormalPojo", uri = "/test/pojo/{id}")
public class ExtendedNormalPojo extends NormalPojo {
  
    public ExtendedNormalPojo() {
        super();
    }

    @Override
    @Siren4JSubEntity
    public Collection<String> getRefs() {
        // TODO Auto-generated method stub
        return super.getRefs();
    }
    
    

}
