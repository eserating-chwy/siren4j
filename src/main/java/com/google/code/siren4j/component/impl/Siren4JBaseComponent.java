package com.google.code.siren4j.component.impl;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.code.siren4j.Siren4J;



public abstract class Siren4JBaseComponent {

    @JsonProperty(value = "class")
    protected String[] componentClass;

    public String[] getComponentClass() {
        return componentClass;
    }

    public void setComponentClass(String[] componentClass) {
        this.componentClass = componentClass;
    }

    /**
     * Uses Jackson to serialize into a json string.
     */
    @Override
    public String toString() {
        String out = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.setDateFormat(new SimpleDateFormat(Siren4J.DEFAULT_DATE_FORMAT));
            out = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return out;
    }
    

}
