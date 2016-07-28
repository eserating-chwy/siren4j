package com.google.code.siren4j.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.code.siren4j.Siren4J;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.text.SimpleDateFormat;

@Provider
public class MessageBodyWriterJSON extends JacksonJsonProvider {

    public MessageBodyWriterJSON() {
        super();
    }

    public MessageBodyWriterJSON(Annotations... annotationsToUse) {
        super(annotationsToUse);
    }

    public MessageBodyWriterJSON(ObjectMapper mapper, Annotations[] annotationsToUse) {
        super(mapper, annotationsToUse);
    }

    public MessageBodyWriterJSON(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {
        ObjectMapper mapper = super.locateMapper(type, mediaType);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new SimpleDateFormat(Siren4J.ISO8601_DATE_FORMAT));
        return mapper;
    }

}
