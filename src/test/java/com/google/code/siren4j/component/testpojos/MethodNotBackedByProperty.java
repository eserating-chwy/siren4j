package com.google.code.siren4j.component.testpojos;


import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JProperty;

@Siren4JEntity(name = "SummaryInfo")
public class MethodNotBackedByProperty {

    private String information;

    @Siren4JProperty
    public String getInfo() {
        return "SummaryInfo";
    }

}
