package com.google.code.siren4j;

/**
 * Useful constants for working with Siren4J.
 */
public class Siren4J {

    private Siren4J() {
    }
    
    /**
     * The Media Type for Siren/JSON (application/vnd.siren+json).
     */
    public static final String JSON_MEDIATYPE = "application/vnd.siren+json"; 
    
    /**
     * Default date format string used for date serialization.
     * <code>yyyy-MM-dd'T'HH:mm:ssZ</code>
     * 
     * This is deprecated because it is not strictly ISO compliant, use
     * {@link Siren4J#ISO8601_DATE_FORMAT} instead. 
     */
    @Deprecated
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * Correct ISO 8601 date format used for date serialization.
     */
    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    
    /**
     * Constant for the reserved property that contains the entity class.
     */
    public static final String CLASS_RESERVED_PROPERTY = "$siren4j.class";
    
    
    

}
