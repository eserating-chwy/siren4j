package com.google.code.siren4j.resource;

public interface Resource {
    
    /**
     * Overrides annotation settings for the uri of this resource.
     * The override ONLY occurs if this method is called.
     * @param uri the uri to override with. Cannot be <code>null</code> or empty.
     */
    public void setOverrideUri(String uri);
    
    /**
     * Retrieve the override uri if set.
     * @return override uri or <code>null</code> if not set.
     */
    public String getOverrideUri();
    
    /**
     * Override annotation settings indicating that if this is a sub entity and
     * if it should be an embedded link. The override ONLY occurs if this method is called.
     * @param isEmbeddedLink
     */
    public void setOverrideEmbeddedLink(Boolean isEmbeddedLink);
    
    /**
     * Retrieve the override embedded link setting if set.
     * @return <code>null</code> if not set.
     */
    public Boolean getOverrideEmbeddedLink();    
    

}
