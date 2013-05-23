package com.google.code.siren4j.converter;

import java.lang.reflect.Field;
import java.util.List;

public class EntityContextImpl implements EntityContext {

    private Object currentObj;
    private List<ReflectedInfo> currentInfo;
    private Field parentField;
    private Object parentObj;
    private List<ReflectedInfo> parentInfo;
    
    public EntityContextImpl(Object currentObj, List<ReflectedInfo> currentInfo) {
       this(currentObj, currentInfo, null, null, null); 
    }

    public EntityContextImpl(Object currentObj, List<ReflectedInfo> currentInfo, Field parentField, Object parentObj,
        List<ReflectedInfo> parentInfo) {
        this.currentObj = currentObj;
        this.currentInfo = currentInfo;
        this.parentField = parentField;
        this.parentObj = parentObj;
        this.parentInfo = parentInfo;
    }

    public Object getCurrentObject() {
        return currentObj;
    }

    public List<ReflectedInfo> getCurrentFieldInfo() {
        return currentInfo;
    }

    public Field getParentField() {
        return parentField;
    }

    public Object getParentObject() {
        return parentObj;
    }

    public List<ReflectedInfo> getParentFieldInfo() {
        return parentInfo;
    }   

    public boolean hasParent() {
       return parentObj != null;
    }

}
