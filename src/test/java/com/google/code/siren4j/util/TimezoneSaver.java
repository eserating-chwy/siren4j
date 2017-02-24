package com.google.code.siren4j.util;

import org.junit.rules.ExternalResource;

import java.util.TimeZone;

public class TimezoneSaver extends ExternalResource {
    private TimeZone savedTimeZone;
    private final TimeZone targetTimezone;
    public TimezoneSaver(String timezone){
        targetTimezone = TimeZone.getTimeZone(timezone);
    }

    @Override
    protected void before() throws Throwable {
        savedTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(targetTimezone);
    }

    @Override
    protected void after() {
        TimeZone.setDefault(savedTimeZone);
    }
}