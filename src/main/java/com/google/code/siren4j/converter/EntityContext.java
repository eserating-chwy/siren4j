package com.google.code.siren4j.converter;

import java.lang.reflect.Field;
import java.util.List;

public interface EntityContext {
    
    public Object getCurrentObject();
    
    public List<ReflectedInfo> getCurrentFieldInfo();
    
    public Field getParentField();
    
    public Object getParentObject();
    
    public List<ReflectedInfo> getParentFieldInfo();
    
    public boolean hasParent();

}
