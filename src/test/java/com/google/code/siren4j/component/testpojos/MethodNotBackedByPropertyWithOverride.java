package com.google.code.siren4j.component.testpojos;


import com.google.code.siren4j.annotations.Siren4JEntity;
import com.google.code.siren4j.annotations.Siren4JProperty;

@Siren4JEntity(name = "SummaryInfo")
public class MethodNotBackedByPropertyWithOverride {

    private String information;

    @Siren4JProperty(name = "summaryInformation")
    public String getInfo() {
        return "SummaryInfo";
    }

}
