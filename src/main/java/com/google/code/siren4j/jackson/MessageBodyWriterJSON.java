package com.google.code.siren4j.jackson;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

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
        return mapper;
    }

}
